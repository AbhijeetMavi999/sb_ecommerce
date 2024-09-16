package com.sb_ecom.controller;

import com.sb_ecom.model.Category;
import com.sb_ecom.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/public/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<String> createCategory(@RequestBody Category category) {
        categoryService.createCategory(category);
        return new ResponseEntity<>("Category created successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
        return new ResponseEntity<>("Category deleted successfully with Id: "+categoryId, HttpStatus.ACCEPTED);
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category, @PathVariable Long categoryId)
    throws ResponseStatusException {
        Category updatedCategory = categoryService.updateCategory(category, categoryId);
        return new ResponseEntity<>(updatedCategory, HttpStatus.ACCEPTED);
    }
}
