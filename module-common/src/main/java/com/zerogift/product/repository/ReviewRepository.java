package com.zerogift.product.repository;

import com.zerogift.member.domain.Member;
import com.zerogift.product.domain.Product;
import com.zerogift.product.domain.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByMemberAndId(Member member, Long reviewId);

    List<Review> findByMember(Member member);

    List<Review> findByProduct(Product product);

    Optional<Review> findByMemberAndProduct(Member member, Product product);
}
