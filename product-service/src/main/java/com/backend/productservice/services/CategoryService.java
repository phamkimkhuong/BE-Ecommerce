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
    public List<CategoryDTO> getAll();
    public CategoryDTO getById(Long id);
    public CategoryDTO save(CategoryDTO category);
    public CategoryDTO update(Long id,CategoryDTO category);
    public boolean delete(Long id);
}
