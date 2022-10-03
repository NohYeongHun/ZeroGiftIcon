package com.zerogift.product.application;

import com.zerogift.product.application.dto.ReviewInput;
import com.zerogift.product.application.dto.ReviewResponse;
import com.zerogift.support.auth.userdetails.LoginInfo;
import java.util.List;

public interface ReviewService {

    // 상품에 관한 유저의 리뷰작성
    ReviewResponse addReview(LoginInfo loginInfo, Long productId, ReviewInput reviewInput);

    // 리뷰 내용 수정
    ReviewResponse modifyReview(LoginInfo loginInfo, Long reviewId, ReviewInput reviewInput);

    // 리뷰 삭제
    void removeReview(LoginInfo loginInfo, Long reviewId);

    // 유저가 쓴 리뷰 리스트 호출
    List<ReviewResponse> userReviewList(LoginInfo loginInfo);

    // 상품에 대한 리뷰 리스트 호출
    List<ReviewResponse> productReviewList(Long productId);

}
