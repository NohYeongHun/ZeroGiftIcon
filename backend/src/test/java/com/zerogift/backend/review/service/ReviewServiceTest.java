package com.zerogift.backend.review.service;

import com.zerogift.backend.acceptance.AcceptanceTest;
import com.zerogift.backend.giftBox.entity.GiftBox;
import com.zerogift.backend.giftBox.repository.GiftBoxRepository;
import com.zerogift.backend.giftBox.service.GiftBoxService;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.member.repository.MemberRepository;
import com.zerogift.backend.pay.entity.PayHistory;
import com.zerogift.backend.pay.repository.PayHistoryRepository;
import com.zerogift.backend.product.entity.Product;
import com.zerogift.backend.product.entity.ProductImage;
import com.zerogift.backend.product.repository.ProductImageRepository;
import com.zerogift.backend.product.repository.ProductRepository;
import com.zerogift.backend.review.entity.Review;
import com.zerogift.backend.review.model.ReviewInput;
import com.zerogift.backend.review.model.ReviewResponse;
import com.zerogift.backend.review.repository.ReviewRepository;
import com.zerogift.backend.security.dto.AdminInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.zerogift.backend.utils.DataMakeUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ReviewServiceTest extends AcceptanceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private PayHistoryRepository payHistoryRepository;

    @Autowired
    private GiftBoxRepository giftBoxRepository;

    @Autowired
    private GiftBoxService giftBoxService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewService reviewService;

    Member member;
    Product product;
    ProductImage productImage;
    PayHistory payHistory;
    GiftBox giftBox;
    AdminInfo adminInfo;

    @BeforeEach
    void setup() throws IOException {
        super.setUp();
        member = memberRepository.save(회원_생성("test2@naver.com", "테스트"));
        product = productRepository.save(상품_생성("테스트 상품", "테스트 상품 설명", member));
        productImage = productImageRepository.save(상품_이미지_생성("https://test.com"));

        payHistory = payHistoryRepository.save(결제_히스트리_생성(product, member, member));
        giftBox = giftBoxRepository.save(선물함_생성(product, member, member, payHistory));
        adminInfo = AdminInfo.builder()
                .id(member.getId())
                .email(member.getEmail())
                .role(member.getRole().name())
                .build();

        giftBoxService.useGiftCon(giftBox.getId(), giftBox.getCode());
    }

    @DisplayName("리뷰 작성 ")
    @Test
    void addReviewTest() {
        ReviewInput reviewInput = ReviewInput.builder()
                .rank(10)
                .description("리뷰 작성 테스트 중입니다.리뷰 작성 테스트 중입니다.")
                .build();
        reviewService.addReview(adminInfo, product.getId(), reviewInput);

        Optional<Review> optionalReview = reviewRepository.findByMemberAndProduct(member, product);

        assertThat(optionalReview).isNotEmpty();

        Review review = optionalReview.get();

        assertThat(review.getRank()).isEqualTo(reviewInput.getRank());
        assertThat(review.getDescription()).isEqualTo(reviewInput.getDescription());
        assertThat(review.getMember().getId()).isEqualTo(member.getId());
        assertThat(review.getProduct().getId()).isEqualTo(product.getId());
    }

    @DisplayName("리뷰 수정")
    @Test
    void modifyReviewTest() {
        ReviewInput reviewInput = ReviewInput.builder()
                .rank(10)
                .description("리뷰 작성 테스트 중입니다.리뷰 작성 테스트 중입니다.")
                .build();
        reviewService.addReview(adminInfo, product.getId(), reviewInput);

        Optional<Review> optionalReview = reviewRepository.findByMemberAndProduct(member, product);
        Review review = optionalReview.get();

        reviewInput = ReviewInput.builder()
                .rank(10)
                .description("리뷰 수정 테스트 중입니다.리뷰 수정 테스트 중입니다.")
                .build();
        reviewService.modifyReview(adminInfo, review.getId(), reviewInput);

        optionalReview = reviewRepository.findByMemberAndProduct(member, product);

        assertThat(optionalReview).isNotEmpty();

        review = optionalReview.get();

        assertThat(review.getRank()).isEqualTo(reviewInput.getRank());
        assertThat(review.getDescription()).isEqualTo(reviewInput.getDescription());
        assertThat(review.getMember().getId()).isEqualTo(member.getId());
        assertThat(review.getProduct().getId()).isEqualTo(product.getId());
    }

    @DisplayName("리뷰 삭제")
    @Test
    void removeReviewTest() {
        ReviewInput reviewInput = ReviewInput.builder()
                .rank(10)
                .description("리뷰 작성 테스트 중입니다.리뷰 작성 테스트 중입니다.")
                .build();
        reviewService.addReview(adminInfo, product.getId(), reviewInput);

        Optional<Review> optionalReview = reviewRepository.findByMemberAndProduct(member, product);
        Review review = optionalReview.get();

        reviewService.removeReview(adminInfo, review.getId());

        optionalReview = reviewRepository.findByMemberAndProduct(member, product);

        assertThat(optionalReview).isEmpty();
    }

    @DisplayName("멤버의 리뷰 리스트")
    @Test
    void userReviewListTest() {
        ReviewInput reviewInput = ReviewInput.builder()
                .rank(10)
                .description("리뷰 작성 테스트 중입니다.리뷰 작성 테스트 중입니다.")
                .build();
        reviewService.addReview(adminInfo, product.getId(), reviewInput);

        List<ReviewResponse> reviewList = reviewService.userReviewList(adminInfo);

        assertThat(reviewList).isNotEmpty();
        assertThat(reviewList.get(0).getMember().getId()).isEqualTo(member.getId());
        assertThat(reviewList.get(0).getProduct().getId()).isEqualTo(product.getId());
    }

    @DisplayName("상품의 리뷰 리스트")
    @Test
    void productReviewListTest() {
        ReviewInput reviewInput = ReviewInput.builder()
                .rank(10)
                .description("리뷰 작성 테스트 중입니다.리뷰 작성 테스트 중입니다.")
                .build();
        reviewService.addReview(adminInfo, product.getId(), reviewInput);

        List<ReviewResponse> reviewList = reviewService.productReviewList(product.getId());

        assertThat(reviewList).isNotEmpty();
        assertThat(reviewList.get(0).getMember().getId()).isEqualTo(member.getId());
        assertThat(reviewList.get(0).getProduct().getId()).isEqualTo(product.getId());
    }
}