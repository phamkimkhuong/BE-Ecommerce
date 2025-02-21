package com.backend.productservice.repository;

import com.backend.productservice.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "categoty", path = "category")
public interface CategorytRepository extends JpaRepository<Category, Long> {

}
