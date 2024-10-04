package com.sb_ecom.service.impl;

import com.sb_ecom.exception.APIException;
import com.sb_ecom.exception.ResourceAlreadyExistException;
import com.sb_ecom.exception.ResourceNotFoundException;
import com.sb_ecom.model.Category;
import com.sb_ecom.model.Product;
import com.sb_ecom.payload.ProductDTO;
import com.sb_ecom.payload.ProductResponse;
import com.sb_ecom.repository.CategoryRepository;
import com.sb_ecom.repository.ProductRepository;
import com.sb_ecom.service.FileService;
import com.sb_ecom.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
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

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductDTO addProduct(Product product, Long categoryId) throws ResourceNotFoundException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category Not Found"));

        Product foundProduct = productRepository.findByProductName(product.getProductName());
        if(foundProduct != null) {
            log.warn("The product with the name of {} is already exist",product.getProductName());
            throw new ResourceAlreadyExistException("The product is already exist by name: "+product.getProductName());
        }

        product.setCategory(category);
        double specialPrice = product.getPrice() - (product.getPrice() * product.getDiscount() / 100);
        product.setSpecialPrice(specialPrice);
        product.setImage("default.png");
        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) throws APIException {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepository.findAll(pageDetails);

        List<Product> products = productPage.getContent();
        if(products.isEmpty()) {
            log.warn("Products not found");
            throw new APIException("Products not found");
        }
        List<ProductDTO> productDTOS = products.stream().map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse searchProductsByCategoryId(Long categoryId) throws ResourceNotFoundException {
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
    public ProductResponse searchProductsByKeyword(String keyword) {
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
        String fileName = fileService.uploadImage(path, image);
        // updating the new file name to product
        product.setImage(fileName);
        // Save updated Product
        Product updatedProduct = productRepository.save(product);
        // return DTO after mapping product to DTO
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }
}
