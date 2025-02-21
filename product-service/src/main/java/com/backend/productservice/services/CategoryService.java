package com.backend.productservice.services;


/*
 * @description
 * @author: pham Kim Khuong
 * @version: 1.0
 * @created: 2/21/2025 12:53 PM
 */

import com.backend.productservice.domain.Category;

import java.util.List;

public interface CategoryService {
    public List<Category> getAll();
    public Category getById(Long id);
    public Category save(Category category);
    public Category update(Category category);
    public void delete(Long id);
}
