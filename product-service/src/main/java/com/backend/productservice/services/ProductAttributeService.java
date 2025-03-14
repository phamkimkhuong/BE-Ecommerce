package com.backend.productservice.services;


/*
 * @description
 * @author: pham Kim Khuong
 * @version: 1.0
 * @created: 2/21/2025 12:53 PM
 */

import com.backend.productservice.dto.ProductAttributeDTO;

public interface ProductAttributeService {
    public ProductAttributeDTO saveProduct(ProductAttributeDTO product);

    public ProductAttributeDTO updateProduct(Long id, ProductAttributeDTO product);

    public boolean deleteProduct(Long id);
}
