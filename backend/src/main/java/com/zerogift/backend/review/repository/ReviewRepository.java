package com.zerogift.backend.review.repository;

import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.product.entity.Product;
import com.zerogift.backend.review.entity.Review;
import com.zerogift.backend.review.model.ReviewResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByMemberAndId(Member member, Long reviewId);

    List<Review> findByMember(Member member);

    List<Review> findByProduct(Product product);

    Optional<Review> findByMemberAndProduct(Member member, Product product);
}
