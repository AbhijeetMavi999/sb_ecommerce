package com.sb_ecom.service;

import com.sb_ecom.exception.ResponseStatusException;
import com.sb_ecom.model.Category;
import com.sb_ecom.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() throws ResponseStatusException {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()) {
            log.error("Categories Not Found");
            throw new ResponseStatusException("Categories Not Found");
        }
        return categories;
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
    public Category updateCategory(Category category, Long categoryId) throws ResponseStatusException {
        Category dbCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException("Categories Not Found with Id: "+categoryId));
        dbCategory.setCategoryName(category.getCategoryName());
        Category updatedCategory = categoryRepository.save(dbCategory);
        return updatedCategory;
    }
}
