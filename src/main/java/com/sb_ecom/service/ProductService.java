package com.sb_ecom.service;

import com.sb_ecom.exception.APIException;
import com.sb_ecom.exception.ResourceNotFoundException;
import com.sb_ecom.model.Product;
import com.sb_ecom.payload.ProductDTO;
import com.sb_ecom.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(Product product, Long categoryId) throws ResourceNotFoundException;

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) throws APIException;

    ProductResponse searchProductsByCategoryId(Long categoryId) throws ResourceNotFoundException;

    ProductResponse searchProductsByKeyword(String keyword);

    ProductDTO updateProductById(ProductDTO productDTO, Long productId) throws ResourceNotFoundException;

    ProductDTO deleteProduct(Long productId) throws ResourceNotFoundException;

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException, ResourceNotFoundException;
}
