package com.backend.productservice.services.serviceImpl;


/*
 * @description
 * @author: Pham Kim khuong
 * @version: 1.0
 * @created: 2/21/2025 12:55 PM
 */

import com.backend.commonservice.model.ItemNotFoundException;
import com.backend.productservice.dto.reponse.ProductReponse;
import com.backend.productservice.dto.request.ProductCreationRequest;
import com.backend.productservice.model.Product;
import com.backend.productservice.repository.ProductRepository;
import com.backend.productservice.services.ProductService;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductServiceImpl implements ProductService {
    ProductRepository productRep;
    ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRep, ModelMapper modelMapper) {
        this.productRep = productRep;
        this.modelMapper = modelMapper;
    }

    //    Convert Entity to DTO
    public ProductReponse toProductReponse(Product product) {
        return modelMapper.map(product, ProductReponse.class);
    }

    //    Convert DTO to Entity
    public Product toProduct(ProductCreationRequest product) {
        return modelMapper.map(product, Product.class);
    }

    @Override
    public List<ProductReponse> getAllProduct() {
        return productRep.findAll().stream().map(this::toProductReponse).toList();
    }

    @Override
    public ProductReponse getProductById(Long id) {
        Product p = productRep.findById(id).orElseThrow(() -> new ItemNotFoundException("Product", "id", id));
        return toProductReponse(p);
    }

    @Transactional
    @Override
    public ProductReponse saveProduct(ProductCreationRequest product) {
        log.info("In method save Product");
        Product p = productRep.save(toProduct(product));
        return toProductReponse(p);
    }

    @Transactional
    @Override
    public ProductReponse updateProduct(Long id, ProductCreationRequest product) {
        Product p = productRep.findById(id).orElseThrow(() -> new ItemNotFoundException("Product", "id", id));
        return toProductReponse(productRep.save(p));
    }

    @Transactional
    @Override
    public boolean deleteProduct(Long id) {
        productRep.deleteById(id);
        return true;
    }
}
