package com.backend.productservice.services.serviceImpl;


/*
 * @description
 * @author: Pham Kim khuong
 * @version: 1.0
 * @created: 2/21/2025 12:55 PM
 */

import com.backend.productservice.domain.Category;
import com.backend.productservice.dto.CategoryDTO;
import com.backend.productservice.repository.CategorytRepository;
import com.backend.productservice.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private CategorytRepository categoryRep;
    @Autowired
    private ModelMapper modelMapper;

    public CategoryServiceImpl(CategorytRepository categoryRep) {
        this.categoryRep = categoryRep;
    }

    //    Convert Entity to DTO
    public Category convertToEntity(CategoryDTO category) {
        return modelMapper.map(category, Category.class);
    }
    //    Convert DTO to Entity
    public CategoryDTO convertToDTO(Category category) {
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public List<CategoryDTO> getAll() {
        return categoryRep.findAll().stream().map(this::convertToDTO).toList();
    }

    @Override
    public CategoryDTO getById(Long id) {
        return categoryRep.findById(id).map(this::convertToDTO).orElse(null);
    }

    @Override
    public CategoryDTO save(CategoryDTO category) {
        Category c = categoryRep.save(convertToEntity(category));
        return convertToDTO(c);
    }

    @Override
    public CategoryDTO update(Long id,CategoryDTO category) {
        categoryRep.findById(id);
        Category c = categoryRep.save(convertToEntity(category));
        return convertToDTO(c);
    }

    @Override
    public boolean delete(Long id) {
        categoryRep.deleteById(id);
        return true;
    }
}
