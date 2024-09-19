package com.sb_ecom.service;

import com.sb_ecom.exception.ResourceNotFound;
import com.sb_ecom.model.Category;
import com.sb_ecom.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() throws ResourceNotFound {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()) {
            log.error("Categories Not Found");
            throw new ResourceNotFound("Categories Not Found");
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
    public Category updateCategory(Category category, Long categoryId) throws ResourceNotFound {
        Category dbCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFound("Categories Not Found with Id: "+categoryId));
        dbCategory.setCategoryName(category.getCategoryName());
        Category updatedCategory = categoryRepository.save(dbCategory);
        return updatedCategory;
    }
}
