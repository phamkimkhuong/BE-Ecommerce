package com.backend.productservice.services.serviceImpl;


/*
 * @description
 * @author: Pham Kim khuong
 * @version: 1.0
 * @created: 2/21/2025 12:55 PM
 */

import com.backend.productservice.domain.Product;
import com.backend.productservice.dto.ProductDTO;
import com.backend.productservice.repository.ProductRepository;
import com.backend.productservice.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRep;
    @Autowired
    private ModelMapper modelMapper;
    public ProductServiceImpl(ProductRepository productRep) {
        this.productRep = productRep;
    }
//    Convert Entity to DTO
    public Product convertToEntity(ProductDTO product) {
        return modelMapper.map(product, Product.class);
    }
//    Convert DTO to Entity
    public ProductDTO convertToDTO(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public List<ProductDTO> getAllProduct() {
        return productRep.findAll().stream().map(this::convertToDTO).toList();
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return productRep.findById(id).map(this::convertToDTO).orElse(null);
    }

    @Override
    public ProductDTO saveProduct(ProductDTO product) {
        Product p = productRep.save(convertToEntity(product));
        return convertToDTO(p);
    }

    @Override
    public ProductDTO updateProduct(ProductDTO product) {
       Product p = productRep.save(convertToEntity(product));
         return convertToDTO(p);
    }

    @Override
    public void deleteProduct(Long id) {
        productRep.deleteById(id);
    }
}
