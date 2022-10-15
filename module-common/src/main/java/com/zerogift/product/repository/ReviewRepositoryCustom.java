package com.zerogift.product.repository;

import com.zerogift.product.application.dto.ReviewQueryModel;
import com.zerogift.product.domain.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepositoryCustom{

    Optional<Review> findReviewByMember(String email, Long reviewId);

    List<ReviewQueryModel> findReviewListByMember(String email);

    List<ReviewQueryModel> findReviewListByProduct(Long productId);

}
