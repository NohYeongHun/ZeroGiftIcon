package com.zerogift.product.repository;

import com.zerogift.member.domain.Member;
import com.zerogift.product.domain.Product;
import com.zerogift.product.domain.ViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewHistoryRepository extends JpaRepository<ViewHistory, Long> {
    boolean existsByMemberAndProduct(Member member, Product product);
}
