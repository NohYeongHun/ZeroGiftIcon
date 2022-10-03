package com.zerogift.product.application;

import com.zerogift.gift.domain.GiftBox;
import com.zerogift.gift.repository.GiftBoxRepository;
import com.zerogift.global.error.code.GiftBoxErrorCode;
import com.zerogift.global.error.code.MemberErrorCode;
import com.zerogift.global.error.code.ProductErrorCode;
import com.zerogift.global.error.code.ReviewErrorCode;
import com.zerogift.global.error.exception.MemberException;
import com.zerogift.global.error.exception.NotFoundGiftBoxException;
import com.zerogift.global.error.exception.ProductException;
import com.zerogift.global.error.exception.ReviewException;
import com.zerogift.member.domain.Member;
import com.zerogift.member.repository.MemberRepository;
import com.zerogift.notice.application.NoticeService;
import com.zerogift.notice.application.dto.EventInfo;
import com.zerogift.notice.domain.NoticeType;
import com.zerogift.product.application.dto.ReviewInput;
import com.zerogift.product.application.dto.ReviewResponse;
import com.zerogift.product.domain.Product;
import com.zerogift.product.domain.Review;
import com.zerogift.product.repository.ProductRepository;
import com.zerogift.product.repository.ReviewRepository;
import com.zerogift.support.auth.userdetails.LoginInfo;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class ReviewServiceImpl implements ReviewService{

    private static final double MILEAGE_ACCUMULATION_RATE = 0.05;

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final GiftBoxRepository giftBoxRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public ReviewResponse addReview(LoginInfo loginInfo, Long productId, ReviewInput reviewInput) {
        // 회원 정보 가져오기
        Member member = memberRepository.findByEmail(loginInfo.getEmail())
            .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 상품 정보 가져오기
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));

        List<GiftBox> giftBoxList = giftBoxRepository.findByRecipientMemberAndProductAndReview(member, product, false);
        if (giftBoxList.isEmpty()) {
            throw new NotFoundGiftBoxException(GiftBoxErrorCode.GIFT_BOX_NOT_FOUND);
        }
        GiftBox giftBox = giftBoxList.get(0);

        giftBox.review();

        // 리뷰 내용 저장
        Review review = reviewRepository.save(Review.builder()
            .rank(reviewInput.getRank())
            .description(reviewInput.getDescription())
            .member(member)
            .product(product)
            .build()
        );

        int point = (int) (giftBox.getPayHistory().getPrice() * MILEAGE_ACCUMULATION_RATE);
        member.mileagePoints((point < 1000 ? point : 1000));

        applicationEventPublisher.publishEvent(new EventInfo( review.getMember(), review.getProduct().getMember(),
            NoticeType.review, review.getId() ));

        // member 와 product 내용 편집해서 출력
        return ReviewResponse.from(review);
    }

    @Override
    public ReviewResponse modifyReview(LoginInfo loginInfo, Long reviewId, ReviewInput reviewInput) {
        // 회원 정보 가져오기
        Member member = memberRepository.findByEmail(loginInfo.getEmail())
            .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 수정할 리뷰 가져오기
        Review review = reviewRepository.findByMemberAndId(member, reviewId)
            .orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));

        // 리뷰 별점과 내용 수정
        review.modify(reviewInput.getRank(), reviewInput.getDescription());

        return ReviewResponse.from(review);
    }

    @Override
    public void removeReview(LoginInfo loginInfo, Long reviewId) {
        // 회원 정보 가져오기
        Member member = memberRepository.findByEmail(loginInfo.getEmail())
            .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 수정할 리뷰 가져오기
        Review review = reviewRepository.findByMemberAndId(member, reviewId)
            .orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));

        // 해당 리뷰 삭제
        reviewRepository.delete(review);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReviewResponse> userReviewList(LoginInfo loginInfo) {
        // 회원 정보 가져오기
        Member member = memberRepository.findByEmail(loginInfo.getEmail())
            .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        // review 에 들어가있는 member 와 product 정보 편집
        List<ReviewResponse> reviewResponseList = reviewRepository.findByMember(member).stream().map(
            x -> ReviewResponse.from(x)).collect(Collectors.toList());
        return reviewResponseList;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReviewResponse> productReviewList(Long productId) {
        // 상품 정보 가져오기
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));

        // review 에 들어가있는 member 와 product 정보 편집
        List<ReviewResponse> reviewResponseList = reviewRepository.findByProduct(product).stream().map(
            x -> ReviewResponse.from(x)).collect(Collectors.toList());
        return reviewResponseList;
    }
}
