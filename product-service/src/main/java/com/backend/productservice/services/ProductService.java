package com.backend.productservice.services;


/*
 * @description
 * @author: pham Kim Khuong
 * @version: 1.0
 * @created: 2/21/2025 12:53 PM
 */

import com.backend.productservice.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    public List<ProductDTO> getAllProduct();

    public ProductDTO getProductById(Long id);

    public ProductDTO saveProduct(ProductDTO product);

    public ProductDTO updateProduct(Long id, ProductDTO product);

    public boolean deleteProduct(Long id);
}
