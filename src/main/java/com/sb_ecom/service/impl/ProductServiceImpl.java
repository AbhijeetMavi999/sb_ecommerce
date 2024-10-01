package com.sb_ecom.service.impl;

import com.sb_ecom.exception.ResourceNotFoundException;
import com.sb_ecom.model.Category;
import com.sb_ecom.model.Product;
import com.sb_ecom.payload.ProductDTO;
import com.sb_ecom.payload.ProductResponse;
import com.sb_ecom.repository.CategoryRepository;
import com.sb_ecom.repository.ProductRepository;
import com.sb_ecom.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDTO addProduct(Product product, Long categoryId) throws ResourceNotFoundException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category Not Found"));

        product.setCategory(category);
        double specialPrice = product.getPrice() - (product.getPrice() * product.getDiscount() / 100);
        product.setSpecialPrice(specialPrice);
        product.setImage("default.png");
        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts() throws ResourceNotFoundException {
        List<Product> products = productRepository.findAll();
        if(products.isEmpty()) {
            log.warn("Products not found");
            throw new ResourceNotFoundException("Products not found");
        }
        List<ProductDTO> productDTOS = products.stream().map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);

        return productResponse;
    }
}
