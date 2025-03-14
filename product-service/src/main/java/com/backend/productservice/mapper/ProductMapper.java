package com.backend.productservice.mapper;

import com.backend.productservice.dto.reponse.ProductCreationReponse;
import com.backend.productservice.dto.request.ProductCreationRequest;
import com.backend.productservice.model.Product;
import com.backend.productservice.model.ProductAttribute;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(ProductCreationRequest request);
    ProductAttribute toProductAttribute(ProductCreationRequest request);
    ProductCreationReponse toProductCreationReponse(Product entity);

}
