package com.sb_ecom.service;

import com.sb_ecom.exception.APIException;
import com.sb_ecom.exception.ResourceNotFoundException;
import com.sb_ecom.model.Category;
import com.sb_ecom.payload.CategoryDTO;
import com.sb_ecom.payload.CategoryResponse;
import com.sb_ecom.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) throws ResourceNotFoundException {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        List<Category> categories = categoryPage.getContent();
        if(categories.isEmpty()) {
            log.error("Categories Not Found");
            throw new ResourceNotFoundException("Categories Not Found");
        }
        // I am using the stream API to map the object because the repository returning the list of categories
        List<CategoryDTO> categoryDTOS = categories.stream().map(
                category -> modelMapper.map(category, CategoryDTO.class)
        ).toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());

        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) throws APIException {
        Category savedCategory = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());
        if(savedCategory != null) {
            log.warn("Category already exist by categoryName: "+categoryDTO.getCategoryName());
            throw new APIException("Category already exist by categoryName: "+categoryDTO.getCategoryName());
        }
        Category category = modelMapper.map(categoryDTO, Category.class);
        categoryRepository.save(category);
        CategoryDTO savedCategoryDTO = modelMapper.map(category, CategoryDTO.class);
        return savedCategoryDTO;
    }

    @Override
    public CategoryDTO deleteCategoryById(Long categoryId) throws ResourceNotFoundException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found by Id: "+categoryId));
        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) throws ResourceNotFoundException {
        Category dbCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categories Not Found with Id: "+categoryId));
        dbCategory.setCategoryName(categoryDTO.getCategoryName());
        Category updatedCategory = categoryRepository.save(dbCategory);

        return modelMapper.map(dbCategory, CategoryDTO.class);
    }
}
