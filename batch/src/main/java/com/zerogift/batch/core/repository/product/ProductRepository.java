package com.zerogift.batch.core.repository.product;

import com.zerogift.batch.core.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
