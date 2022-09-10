package com.example.demo.product.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.demo.product.entity.Product;
import com.example.demo.product.type.Status;
import com.example.demo.product.type.Category;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
    Page<Product> findByCategory(Category category, Pageable pageable);
    Page<Product> findByStatusAndCategoryIn(Status status, List<Category> category, Pageable pageable);
}
