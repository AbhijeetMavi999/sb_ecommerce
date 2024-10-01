package com.sb_ecom.service;

import com.sb_ecom.exception.ResourceNotFoundException;
import com.sb_ecom.model.Product;
import com.sb_ecom.payload.ProductDTO;
import com.sb_ecom.payload.ProductResponse;

public interface ProductService {
    ProductDTO addProduct(Product product, Long categoryId) throws ResourceNotFoundException;

    ProductResponse getAllProducts() throws ResourceNotFoundException;
}
