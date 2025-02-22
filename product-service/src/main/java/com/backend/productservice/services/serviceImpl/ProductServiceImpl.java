package com.backend.productservice.services.serviceImpl;


/*
 * @description
 * @author: Pham Kim khuong
 * @version: 1.0
 * @created: 2/21/2025 12:55 PM
 */

import com.backend.commonservice.model.ItemNotFoundException;
import com.backend.productservice.domain.Product;
import com.backend.productservice.dto.ProductDTO;
import com.backend.productservice.repository.ProductRepository;
import com.backend.productservice.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return productRep.findById(id).map(this::convertToDTO).orElseThrow(
                ()-> new ItemNotFoundException("Product","id",id)
        );
    }
    @Transactional
    @Override
    public ProductDTO saveProduct(ProductDTO product) {
        Product p = productRep.save(convertToEntity(product));
        return convertToDTO(p);
    }

    @Transactional
    @Override
    public ProductDTO updateProduct(Long id,ProductDTO product) {
        productRep.findById(id);
        Product p = productRep.save(convertToEntity(product));
        return convertToDTO(p);
    }

    @Transactional
    @Override
    public boolean deleteProduct(Long id) {
        productRep.deleteById(id);
        return true;
    }
}
