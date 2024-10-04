package com.sb_ecom.controller;

import com.sb_ecom.config.AppConstant;
import com.sb_ecom.exception.APIException;
import com.sb_ecom.exception.ResourceNotFoundException;
import com.sb_ecom.model.Product;
import com.sb_ecom.payload.ProductDTO;
import com.sb_ecom.payload.ProductResponse;
import com.sb_ecom.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody Product product,
                                                 @PathVariable Long categoryId) throws ResourceNotFoundException {
        ProductDTO productDTO = productService.addProduct(product, categoryId);
        return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstant.SORT_PRODUCT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder
    ) throws APIException {
        ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    // Get Products by categoryId
    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategoryId(@PathVariable Long categoryId) throws ResourceNotFoundException {
        ProductResponse productResponse = productService.searchProductsByCategoryId(categoryId);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    // Get Product by keyword
    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword) {
        ProductResponse productResponse = productService.searchProductsByKeyword(keyword);
        return new ResponseEntity<>(productResponse, HttpStatus.FOUND);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductDTO> updateProductById(@RequestBody ProductDTO productDTO, @PathVariable Long productId) throws ResourceNotFoundException {
        ProductDTO updatedProductDTO = productService.updateProductById(productDTO, productId);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId) throws ResourceNotFoundException{
        ProductDTO deletedProductDTO = productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProductDTO, HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam("image") MultipartFile image) throws IOException, ResourceNotFoundException {
        ProductDTO updatedProduct = productService.updateProductImage(productId, image);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
}
