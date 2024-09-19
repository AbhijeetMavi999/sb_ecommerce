package com.sb_ecom.service;

import com.sb_ecom.exception.APIException;
import com.sb_ecom.exception.ResourceNotFoundException;
import com.sb_ecom.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories() throws ResourceNotFoundException;

    void createCategory(Category category) throws APIException;

    void deleteCategoryById(Long categoryId);

    Category updateCategory(Category category, Long categoryId) throws ResourceNotFoundException;
}
