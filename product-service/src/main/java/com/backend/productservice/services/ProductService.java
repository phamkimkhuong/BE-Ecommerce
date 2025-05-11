package com.backend.productservice.services;

/*
 * @description
 * @author: pham Kim Khuong
 * @version: 1.0
 * @created: 2/21/2025 12:53 PM
 */

import com.backend.commonservice.event.ProductEvent;
import com.backend.productservice.dto.reponse.ProductReponse;
import com.backend.productservice.dto.request.ProductCreationRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<ProductReponse> getAllProduct();

    ProductReponse getProductById(Long id);

    ProductReponse saveProduct(ProductCreationRequest product, MultipartFile hinhAnh);

    ProductReponse updateProduct(Long id, ProductCreationRequest product);

    boolean deleteProduct(Long id);

    ProductReponse checkProductAvailability(Long productId, int quantity);

    void updateQuantityProduct(List<ProductEvent> ds);
}