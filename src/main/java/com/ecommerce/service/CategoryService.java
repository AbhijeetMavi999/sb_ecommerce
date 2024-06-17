package com.ecommerce.service;

import com.ecommerce.model.Category;

import java.util.List;

public interface CategoryService {

    public List<Category> getAllCategory();

    void createCategory(Category category);

    void deleteCategory(Long categoryId);

    Category updateCategory(Category category, Long categoryId);
}
