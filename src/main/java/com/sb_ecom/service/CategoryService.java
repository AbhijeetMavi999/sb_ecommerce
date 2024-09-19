package com.sb_ecom.service;

import com.sb_ecom.exception.APIException;
import com.sb_ecom.exception.ResourceNotFoundException;
import com.sb_ecom.model.Category;
import com.sb_ecom.payload.CategoryDTO;
import com.sb_ecom.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) throws ResourceNotFoundException;

    CategoryDTO createCategory(CategoryDTO categoryDTO) throws APIException;

    CategoryDTO deleteCategoryById(Long categoryId) throws ResourceNotFoundException;

    CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) throws ResourceNotFoundException;
}
