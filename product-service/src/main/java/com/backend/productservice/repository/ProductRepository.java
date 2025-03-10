package com.backend.productservice.repository;

import com.backend.productservice.model.Product;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//@RepositoryRestResource(collectionResourceRel = "product", path = "product")
@RepositoryRestResource
@Hidden
public interface ProductRepository extends JpaRepository<Product, Long> {

}
