package com.backend.cartservice.event;

import com.backend.cartservice.services.CartItemService;
import com.backend.cartservice.services.CartService;
import com.backend.commonservice.dto.reponse.CartResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.RetriableException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class OrderConsumer {
    CartItemService cartItemService;
    CartService cartService;

    @RetryableTopic(
            // 2 lần thử lại + 1 lần DLQ
            // Mặc định là 3 lần thử lại
            backoff = @Backoff(delay = 1000, multiplier = 2.0, maxDelay = 10000),
            dltStrategy = DltStrategy.FAIL_ON_ERROR,
            // Thử lại khi ngoại lệ là RuntimeException hoặc RetriableException
            include = {RuntimeException.class, RetriableException.class})
    @KafkaListener(topics = "cart-events")
    public void consumeOrder(String customerId) {
        log.info("Nhận được message xóa san phẩm trong giỏ hàng: {}", customerId);
        Long customerIdLong = Long.valueOf(customerId);
        Optional<CartResponse> cart = cartService.getCartForKafkaConsumer(customerIdLong);
        if (cart.isPresent()) {
            cartItemService.deleteCartItemByCartId(cart.get().getId());
            log.info("Đã xóa tất cả sản phẩm trong giỏ hàng của khách hàng có ID: {}", customerId);
        } else {
            log.error("Không tìm thấy giỏ hàng của khách hàng có ID: {}", customerId);
        }
    }
}
