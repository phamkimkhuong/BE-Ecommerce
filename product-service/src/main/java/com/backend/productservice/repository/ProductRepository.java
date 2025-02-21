package com.backend.productservice.repository;

import com.backend.productservice.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//@RepositoryRestResource(collectionResourceRel = "product", path = "product")
@RepositoryRestResource
public interface ProductRepository extends JpaRepository<Product, Long> {

}
