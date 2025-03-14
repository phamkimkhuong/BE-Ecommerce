package com.backend.productservice.services;


/*
 * @description
 * @author: pham Kim Khuong
 * @version: 1.0
 * @created: 2/21/2025 12:53 PM
 */

import com.backend.productservice.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAll();

    CategoryDTO getById(Long id);

    CategoryDTO save(CategoryDTO category);

    CategoryDTO update(Long id, CategoryDTO category);

    boolean delete(Long id);
}
