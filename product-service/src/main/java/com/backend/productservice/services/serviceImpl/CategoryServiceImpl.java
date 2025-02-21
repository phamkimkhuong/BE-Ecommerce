package com.backend.productservice.services.serviceImpl;


/*
 * @description
 * @author: Pham Kim khuong
 * @version: 1.0
 * @created: 2/21/2025 12:55 PM
 */

import com.backend.productservice.domain.Category;
import com.backend.productservice.repository.CategorytRepository;
import com.backend.productservice.services.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private CategorytRepository categoryRep;
    public CategoryServiceImpl(CategorytRepository categoryRep) {
        this.categoryRep = categoryRep;
    }

    @Override
    public List<Category> getAll() {
        return categoryRep.findAll();
    }

    @Override
    public Category getById(Long id) {
        return categoryRep.findById(id).get();
    }

    @Override
    public Category save(Category category) {
        return categoryRep.save(category);
    }

    @Override
    public Category update(Category category) {
        return categoryRep.save(category);
    }

    @Override
    public void delete(Long id) {
        categoryRep.deleteById(id);
    }
}
