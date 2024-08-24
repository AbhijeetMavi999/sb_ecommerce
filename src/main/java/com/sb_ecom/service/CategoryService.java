package com.sb_ecom.service;

import com.sb_ecom.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();

    void createCategory(Category category);
}
