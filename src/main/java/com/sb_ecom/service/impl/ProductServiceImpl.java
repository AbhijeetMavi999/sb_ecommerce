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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

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
    public ProductResponse getAllProducts() {
        List<Product> products = productRepository.findAll();
        if(products.isEmpty()) {
            log.warn("Products not found");
        }
        List<ProductDTO> productDTOS = products.stream().map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse getProductsByCategoryId(Long categoryId) throws ResourceNotFoundException {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
            new ResourceNotFoundException("Category not found by id: "+categoryId)
        );

        List<Product> products = productRepository.findByCategory(category);
        if(products.isEmpty()) {
            log.warn("Products not found by category id: {}", categoryId);
            throw new ResourceNotFoundException("Products not found by category id: "+categoryId);
        }
        List<ProductDTO> productDTOS = products.stream().map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword) {
        List<Product> products = productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%');
        if(products.isEmpty()) {
            log.warn("Products not found by keyword: {}", keyword);
        }

        List<ProductDTO> productDTOS = products.stream().map((product) -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductDTO updateProductById(ProductDTO productDTO, Long productId) throws ResourceNotFoundException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not Found by Id: "+productId));

        product.setProductName(productDTO.getProductName());
        product.setImage(productDTO.getImage());
        product.setDescription(productDTO.getDescription());
        product.setQuantity(productDTO.getQuantity());
        product.setPrice(productDTO.getPrice());
        product.setDiscount(productDTO.getDiscount());
        double specialPrice = productDTO.getPrice() - (productDTO.getPrice() * productDTO.getDiscount() / 100);
        product.setSpecialPrice(specialPrice);

        productRepository.save(product);
        ProductDTO updatedProductDTO = modelMapper.map(product, ProductDTO.class);

        return updatedProductDTO;
    }

    @Override
    public ProductDTO deleteProduct(Long productId) throws ResourceNotFoundException {
        Product product = productRepository.findById(productId).orElseThrow( () ->
                new ResourceNotFoundException("Product not found by id: "+productId)
        );
        productRepository.deleteById(productId);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException, ResourceNotFoundException {
        // Get the product from DB
        Product product = productRepository.findById(productId).orElseThrow( () ->
                new ResourceNotFoundException("Product not found by id: "+productId)
        );
        // upload image to server

        // Get the file name of uploaded image
        String path = "images/";
        String fileName = uploadImage(path, image);
        // updating the new file name to product
        product.setImage(fileName);
        // Save updated Product
        Product updatedProduct = productRepository.save(product);
        // return DTO after mapping product to DTO
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    private String uploadImage(String path, MultipartFile file) throws IOException {
        // File name of current / original file
        String originalFileName = file.getOriginalFilename();
        // Generate a unique file name to avoid naming conflicts
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String filePath = path + File.separator + fileName;
        // Check path if exist and create
        File folder = new File(path);
        if(!folder.exists())
            folder.mkdir();
        // upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));
        // returning file name
        return fileName;
    }
}
