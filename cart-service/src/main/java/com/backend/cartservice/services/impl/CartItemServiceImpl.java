package com.backend.cartservice.services.impl;

import com.backend.cartservice.dto.request.CreateCartItem;
import com.backend.cartservice.dto.request.UpdateCartItem;
import com.backend.commonservice.dto.reponse.CartItemReponse;
import com.backend.cartservice.entity.CartItem;
import com.backend.cartservice.repository.CartItemRepository;
import com.backend.cartservice.repository.OpenFeignClient.ProductClient;
import com.backend.cartservice.services.CartItemService;
import com.backend.commonservice.configuration.openFeign.FeignResponseException;
import com.backend.commonservice.model.AppException;
import com.backend.commonservice.model.ErrorMessage;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    private final ModelMapper modelMapper;

    private final ProductClient productClient; // Thêm ProductClient để kiểm tra số lượng sản phẩm

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CartItemServiceImpl.class);

    public CartItemServiceImpl(ModelMapper modelMapper, CartItemRepository cartItemRepository, ProductClient productClient) {
        this.cartItemRepository = cartItemRepository;
        this.productClient = productClient;
        this.modelMapper = modelMapper;
    }

    // Convert Entity to DTO
    public CartItemReponse toCartItemRes(CartItem cartItem) {
        return modelMapper.map(cartItem, CartItemReponse.class);
    }

    // Convert DTO to Entity
    public CartItem toCartItem(CreateCartItem cartItem) {
        return modelMapper.map(cartItem, CartItem.class);
    }

    @Transactional
    // Thêm chi tiết giỏ hàng
    @PreAuthorize("hasAuthority('ADMIN') or @cartSecurityExpression.isCartOwner(#cartItem.cartId)")
    public CartItemReponse addCartItem(CreateCartItem cartItem) {
        // Kiểm tra số lượng sản phẩm trong kho trước khi thêm vào giỏ hàng
        checkProductAvailability(cartItem.getProductId(), cartItem.getQuantity());
        CartItem cartItemEntity;
        // TH1 : nếu đã có sản phẩm theo cartId và productId thì cập nhật số lượng
        Optional<CartItem> existingCartItem = cartItemRepository.findByCartIdAndProductId(cartItem.getCartId(), cartItem.getProductId());
        if (existingCartItem.isPresent()) {
            // Cập nhật số lượng cho sản phẩm đã tồn tại
            CartItem existingItem = existingCartItem.get();
            existingItem.setQuantity(existingItem.getQuantity() + cartItem.getQuantity());
            existingItem.setPrice(cartItem.getPrice()); // Cập nhật giá nếu có thay đổi
            cartItemEntity = existingItem;
            log.info("Cập nhật số lượng cho sản phẩm đã tồn tại trong giỏ hàng: cartId={}, productId={}", cartItem.getCartId(), cartItem.getProductId());
        } else {
            // TH2 : nếu không có sản phẩm theo cartId và productId thì thêm mới
            cartItemEntity = toCartItem(cartItem); // Chuyển đổi từ DTO sang Entity
            log.info("Thêm mới sản phẩm vào giỏ hàng: cartId={}, productId={}", cartItem.getCartId(), cartItem.getProductId());
        }
        CartItem c = cartItemRepository.save(cartItemEntity);
        return toCartItemRes(c);
    }
    // Lấy tất cả chi tiết giỏ hàng của giỏ hàng cụ thể
    public List<CartItem> getCartItems(Long cartId) {
        return cartItemRepository.findByCartId(cartId); // Lấy tất cả CartItem theo cartId
    }

    // Cập nhật chi tiết giỏ hàng
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or @cartSecurityExpression.isCartOwner(#cartItem.cartId)")
    public CartItemReponse updateCartItem(UpdateCartItem cartItem) {
        log.info("Cập nhật chi tiết giỏ hàng với ID: {}", cartItem.getId());
        // Kiểm tra nếu CartItem đã tồn tại
        CartItem existingCartItem = cartItemRepository.findById(cartItem.getId()).orElseThrow(() -> new AppException(ErrorMessage.CART_ITEM_NOT_FOUND));
        // Kiểm tra số lượng sản phẩm trong kho trước khi cập nhật
        checkProductAvailability(cartItem.getProductId(), cartItem.getQuantity());
        // Cập nhật giá trị của CartItem
        existingCartItem.setPrice(cartItem.getPrice());
        existingCartItem.setProductId(cartItem.getProductId());
        existingCartItem.setQuantity(cartItem.getQuantity());
        CartItem cartItemSave = cartItemRepository.save(existingCartItem);
        return toCartItemRes(cartItemSave); // Hoặc cartItemRepository.merge(existingCartItem);
    }

    // Xóa chi tiết giỏ hàng
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or @cartSecurityExpression.isCartItemOwner(#cartItemId)")
    public boolean deleteCartItem(Long cartItemId) {
        log.info("Xóa chi tiết giỏ hàng với ID: {}", cartItemId);
        if (cartItemRepository.existsById(cartItemId)) {
            cartItemRepository.deleteById(cartItemId); // Xóa chi tiết giỏ hàng nếu tồn tại
            return true; // Trả về true nếu xóa thành công
        } else {
            throw new AppException(ErrorMessage.CART_ITEM_NOT_FOUND); // Thông báo lỗi nếu không tìm thấy
        }
    }

    @Override
    @Transactional
    public void deleteCartItemByCartId(Long cartId) {
        log.info("CartItem gọi xóa tất cả sản phẩm trong giỏ hàng với ID: {}", cartId);
        cartItemRepository.deleteCartItemByCart_Id(cartId);
    }

    // Lấy chi tiết giỏ hàng theo ID
    public CartItem getCartItemById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId).orElse(null); // Trả về null nếu không tìm thấy
    }

    private void checkProductAvailability(Long productId, int quantity) {
        try {
            productClient.checkProductAvailability(productId, quantity);
        } catch (FeignResponseException e) {
            log.error("Lỗi khi kiểm tra số lượng sản phẩm: status={}, body={}", e.getStatus(), e.getResponseBody());
            // Xử lý dựa trên mã lỗi HTTP
            if (e.getStatus() == 404) {
                throw new AppException(ErrorMessage.PRODUCT_NOT_FOUND);
            } else if (e.getResponseBody().contains("\"code\":421")) {
                throw new AppException(ErrorMessage.PRODUCT_QUANTITY_NOT_ENOUGH);
            }
        } catch (Exception e) {
            log.error("Lỗi không xác định: {}", e.getMessage());
            throw new RuntimeException("Dịch vụ sản phẩm lỗi");
        }
    }
}
