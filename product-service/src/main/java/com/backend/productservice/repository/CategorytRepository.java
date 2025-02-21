package com.backend.productservice.repository;

import com.backend.productservice.domain.Category;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
@Hidden
public interface CategorytRepository extends JpaRepository<Category, Long> {

}
