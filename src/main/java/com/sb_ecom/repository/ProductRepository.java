package com.sb_ecom.repository;

import com.sb_ecom.model.Category;
import com.sb_ecom.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);

    List<Product> findByProductNameLikeIgnoreCase(String keyword);
}
