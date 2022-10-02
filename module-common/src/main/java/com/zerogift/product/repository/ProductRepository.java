package com.zerogift.product.repository;

import com.zerogift.member.domain.Member;
import com.zerogift.product.domain.Category;
import com.zerogift.product.domain.Product;
import com.zerogift.product.domain.Status;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
    Page<Product> findByCategory(Category category, Pageable pageable);
    Page<Product> findByStatusAndCategoryIn(Status status, List<Category> category, Pageable pageable);
    Page<Product> findByMember(Member member, Pageable pageable);
    Page<Product> findByStatusAndNameContainsOrStatusAndDescriptionContains(Status status1, String name, Status status2, String description, Pageable pageable);
}
