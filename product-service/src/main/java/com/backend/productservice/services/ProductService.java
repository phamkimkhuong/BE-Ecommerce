package com.backend.productservice.services;


/*
 * @description
 * @author: pham Kim Khuong
 * @version: 1.0
 * @created: 2/21/2025 12:53 PM
 */

import com.backend.productservice.dto.reponse.ProductReponse;
import com.backend.productservice.dto.request.ProductCreationRequest;

import java.util.List;

public interface ProductService {
    public List<ProductReponse> getAllProduct();

    public ProductReponse getProductById(Long id);

    public ProductReponse saveProduct(ProductCreationRequest product);

    public ProductReponse updateProduct(Long id, ProductCreationRequest product);

    public boolean deleteProduct(Long id);
}
