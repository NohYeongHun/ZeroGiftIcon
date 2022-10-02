package com.zerogift.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static com.zerogift.utils.DataMakeUtils.결제_히스트리_생성;
import static com.zerogift.utils.DataMakeUtils.상품_생성;
import static com.zerogift.utils.DataMakeUtils.선물함_생성;
import static com.zerogift.utils.DataMakeUtils.회원_생성;

import com.zerogift.acceptance.AcceptanceTest;
import com.zerogift.gift.application.GiftBoxService;
import com.zerogift.gift.domain.GiftBox;
import com.zerogift.gift.repository.GiftBoxRepository;
import com.zerogift.member.domain.Member;
import com.zerogift.member.repository.MemberRepository;
import com.zerogift.notice.application.NoticeService;
import com.zerogift.pay.domain.PayHistory;
import com.zerogift.pay.repository.PayHistoryRepository;
import com.zerogift.product.application.ProductService;
import com.zerogift.product.application.ReviewService;
import com.zerogift.product.application.dto.ReviewInput;
import com.zerogift.product.application.dto.ReviewResponse;
import com.zerogift.product.domain.Product;
import com.zerogift.product.domain.Review;
import com.zerogift.product.repository.ProductImageRepository;
import com.zerogift.product.repository.ProductRepository;
import com.zerogift.product.repository.ReviewRepository;
import com.zerogift.support.auth.userdetails.AdminInfo;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;

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

    @Autowired
    private ProductService productService;

    @MockBean
    private NoticeService noticeService;

    Member member;
    Member member1;
    Member member2;

    Product product;
    Product product1;
    Product product2;

    PayHistory payHistory;
    PayHistory payHistory1;
    PayHistory payHistory2;
    PayHistory payHistory3;
    PayHistory payHistory4;

    GiftBox giftBox;
    GiftBox giftBox1;
    GiftBox giftBox2;
    GiftBox giftBox3;
    GiftBox giftBox4;

    AdminInfo adminInfo;
    AdminInfo adminInfo1;
    AdminInfo adminInfo2;

    @BeforeEach
    void setup() throws IOException {
        super.setUp();
        member = memberRepository.save(회원_생성("test@naver.com", "테스트",  "test_id"));
        member1 = memberRepository.save(회원_생성("hong@naver.com", "홍길동", "test1_id"));
        member2 = memberRepository.save(회원_생성("kim@naver.com", "김철수", "test2_id"));

        product = productRepository.save(상품_생성("테스트 상품", "테스트 상품 설명", member));
        product1 = productRepository.save(상품_생성("테스트 상품1", "테스트 상품 설명1", member));
        product2 = productRepository.save(상품_생성("테스트 상품2", "테스트 상품 설명2", member));

        payHistory = payHistoryRepository.save(결제_히스트리_생성(product, member, member));
        payHistory1 = payHistoryRepository.save(결제_히스트리_생성(product1, member1, member));
        payHistory2 = payHistoryRepository.save(결제_히스트리_생성(product2, member2, member));
        payHistory3 = payHistoryRepository.save(결제_히스트리_생성(product, member, member1));
        payHistory4 = payHistoryRepository.save(결제_히스트리_생성(product, member1, member2));

        giftBox = giftBoxRepository.save(선물함_생성(product, payHistory.getFromMember(), payHistory.getToMember(), payHistory));
        giftBox1 = giftBoxRepository.save(선물함_생성(product1, payHistory1.getFromMember(), payHistory1.getToMember(), payHistory1));
        giftBox2 = giftBoxRepository.save(선물함_생성(product2, payHistory2.getFromMember(), payHistory2.getToMember(), payHistory2));
        giftBox3 = giftBoxRepository.save(선물함_생성(product, payHistory3.getFromMember(), payHistory3.getToMember(), payHistory3));
        giftBox4 = giftBoxRepository.save(선물함_생성(product, payHistory4.getFromMember(), payHistory4.getToMember(), payHistory4));

        adminInfo = AdminInfo.builder()
            .id(member.getId())
            .email(member.getEmail())
            .role(member.getRole().name())
            .build();
        adminInfo1 = AdminInfo.builder()
            .id(member1.getId())
            .email(member1.getEmail())
            .role(member1.getRole().name())
            .build();
        adminInfo2 = AdminInfo.builder()
            .id(member2.getId())
            .email(member2.getEmail())
            .role(member2.getRole().name())
            .build();

        giftBoxService.useGiftCon(giftBox.getId(), giftBox.getCode());
        giftBoxService.useGiftCon(giftBox1.getId(), giftBox1.getCode());
        giftBoxService.useGiftCon(giftBox2.getId(), giftBox2.getCode());
        giftBoxService.useGiftCon(giftBox3.getId(), giftBox3.getCode());
        giftBoxService.useGiftCon(giftBox4.getId(), giftBox4.getCode());

        // sse 기능 정지
        doNothing().when(noticeService).sendEvent(any(), any(), any(), any());
    }

    @DisplayName("리뷰 작성 & 포인트 적립")
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

        GiftBox giftBox = giftBoxRepository.findAll().get(0);
        assertThat(giftBox.isReview()).isTrue();

        // 포인트 적립 확인
        Optional<Member> optionalMember = memberRepository.findByEmail(adminInfo.getEmail());
        Member member = optionalMember.get();
        assertThat(member.getPoint()).isEqualTo(50);
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
            .rank(5)
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
        // 상품 리뷰 추가
        ReviewInput reviewInput = ReviewInput.builder()
            .rank(10)
            .description("리뷰 작성 테스트 중입니다.리뷰 작성 테스트 중입니다.")
            .build();
        reviewService.addReview(adminInfo, product.getId(), reviewInput);

        // 상품1 리뷰 추가
        reviewInput = ReviewInput.builder()
            .rank(5)
            .description("리뷰 작성 테스트 중입니다2.리뷰 작성 테스트 중입니다2.")
            .build();
        reviewService.addReview(adminInfo, product1.getId(), reviewInput);

        // 상품2 리뷰 추가
        reviewInput = ReviewInput.builder()
            .rank(8)
            .description("리뷰 작성 테스트 중입니다3.리뷰 작성 테스트 중입니다3.")
            .build();
        reviewService.addReview(adminInfo, product2.getId(), reviewInput);

        List<ReviewResponse> reviewList = reviewService.userReviewList(adminInfo);

        assertThat(reviewList).isNotEmpty();
        assertThat(reviewList.get(0).getMember().getId()).isEqualTo(member.getId());
        assertThat(reviewList.get(0).getProduct().getId()).isEqualTo(product.getId());
        assertThat(reviewList.get(1).getMember().getId()).isEqualTo(member.getId());
        assertThat(reviewList.get(1).getProduct().getId()).isEqualTo(product1.getId());
        assertThat(reviewList.get(2).getMember().getId()).isEqualTo(member.getId());
        assertThat(reviewList.get(2).getProduct().getId()).isEqualTo(product2.getId());
    }

    @DisplayName("상품의 리뷰 리스트")
    @Test
    void productReviewListTest() {

        // 사용자가 작성한 상품 리뷰 추가
        ReviewInput reviewInput = ReviewInput.builder()
            .rank(10)
            .description("리뷰 작성 테스트 중입니다.리뷰 작성 테스트 중입니다.")
            .build();
        reviewService.addReview(adminInfo, product.getId(), reviewInput);

        // 사용자1이 작성한 상품 리뷰 추가
        reviewInput = ReviewInput.builder()
            .rank(10)
            .description("리뷰 작성 테스트 중입니다1.리뷰 작성 테스트 중입니다1.")
            .build();
        reviewService.addReview(adminInfo1, product.getId(), reviewInput);

        // 사용자2가 작성한 상품2 리뷰 추가
        reviewInput = ReviewInput.builder()
            .rank(10)
            .description("리뷰 작성 테스트 중입니다2.리뷰 작성 테스트 중입니다2.")
            .build();
        reviewService.addReview(adminInfo2, product.getId(), reviewInput);

        List<ReviewResponse> reviewList = reviewService.productReviewList(product.getId());

        assertThat(reviewList).isNotEmpty();
        assertThat(reviewList.get(0).getMember().getId()).isEqualTo(member.getId());
        assertThat(reviewList.get(0).getProduct().getId()).isEqualTo(product.getId());
        assertThat(reviewList.get(1).getMember().getId()).isEqualTo(member1.getId());
        assertThat(reviewList.get(1).getProduct().getId()).isEqualTo(product.getId());
        assertThat(reviewList.get(2).getMember().getId()).isEqualTo(member2.getId());
        assertThat(reviewList.get(2).getProduct().getId()).isEqualTo(product.getId());
    }
}