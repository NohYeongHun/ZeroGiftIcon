package com.zerogift.backend.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zerogift.backend.product.entity.Product;
import com.zerogift.backend.product.entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findAllByProduct(Product product);
    Long countByUrl(String url);
}
