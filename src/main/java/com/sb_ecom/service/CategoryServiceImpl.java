package com.sb_ecom.service;

import com.sb_ecom.model.Category;
import com.sb_ecom.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategoryById(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        Optional<Category> updateCategory = categoryRepository.findById(categoryId);
        Category updatedCategory = new Category();
        if(updateCategory.isPresent()) {
            Category existingCategory = updateCategory.get();
            existingCategory.setCategoryName(category.getCategoryName());
            updatedCategory = categoryRepository.save(existingCategory);
        }
        return updatedCategory;
    }
}
