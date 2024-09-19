package com.sb_ecom.controller;

import com.sb_ecom.config.AppConstant;
import com.sb_ecom.exception.APIException;
import com.sb_ecom.exception.ResourceNotFoundException;
import com.sb_ecom.model.Category;
import com.sb_ecom.payload.CategoryDTO;
import com.sb_ecom.payload.CategoryResponse;
import com.sb_ecom.service.CategoryService;
import jakarta.validation.Valid;
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
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstant.SORT_CATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder
    ) throws ResourceNotFoundException {
        CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) throws APIException {
        CategoryDTO savedCategory = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategoryById(@PathVariable Long categoryId) throws ResourceNotFoundException {
        CategoryDTO deletedCategory = categoryService.deleteCategoryById(categoryId);
        return new ResponseEntity<>(deletedCategory, HttpStatus.ACCEPTED);
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDTO categoryDTO)
    throws ResourceNotFoundException {
        CategoryDTO updatedCategory = categoryService.updateCategory(categoryId, categoryDTO);
        return new ResponseEntity<>(updatedCategory, HttpStatus.ACCEPTED);
    }
}
