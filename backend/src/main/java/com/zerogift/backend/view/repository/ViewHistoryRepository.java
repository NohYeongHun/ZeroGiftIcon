package com.zerogift.backend.view.repository;

import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.product.entity.Product;
import com.zerogift.backend.view.entity.ViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewHistoryRepository extends JpaRepository<ViewHistory, Long> {

    long countByProduct(Product product);

    long countByMemberAndProduct(Member member, Product product);
}
