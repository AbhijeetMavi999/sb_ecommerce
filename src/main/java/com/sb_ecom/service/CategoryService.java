package com.sb_ecom.service;

import com.sb_ecom.exception.ResourceNotFound;
import com.sb_ecom.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories() throws ResourceNotFound;

    void createCategory(Category category);

    void deleteCategoryById(Long categoryId);

    Category updateCategory(Category category, Long categoryId) throws ResourceNotFound;
}
