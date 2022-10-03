package com.zerogift.product.repository;

import com.zerogift.product.domain.Product;
import com.zerogift.product.domain.ProductImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findAllByProduct(Product product);
    Long countByUrl(String url);
}
