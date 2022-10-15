package com.zerogift.product.application;

import com.zerogift.gift.domain.GiftBox;
import com.zerogift.gift.repository.GiftBoxRepository;
import com.zerogift.global.error.code.ReviewErrorCode;
import com.zerogift.global.error.exception.MemberException;
import com.zerogift.global.error.exception.NotFoundGiftBoxException;
import com.zerogift.global.error.exception.ProductException;
import com.zerogift.global.error.exception.ReviewException;
import com.zerogift.member.domain.Member;
import com.zerogift.member.repository.MemberRepository;
import com.zerogift.notice.application.dto.EventInfo;
import com.zerogift.notice.domain.NoticeType;
import com.zerogift.pay.domain.PointHistory;
import com.zerogift.pay.domain.PointType;
import com.zerogift.pay.repository.PointHistoryRepository;
import com.zerogift.product.application.dto.ReviewInput;
import com.zerogift.product.application.dto.ReviewResponse;
import com.zerogift.product.domain.Product;
import com.zerogift.product.domain.Review;
import com.zerogift.product.repository.ProductRepository;
import com.zerogift.product.repository.ReviewRepository;
import com.zerogift.support.auth.userdetails.LoginInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.zerogift.global.error.code.GiftBoxErrorCode.GIFT_BOX_NOT_FOUND;
import static com.zerogift.global.error.code.GiftBoxErrorCode.GIFT_BOX_NOT_USE;
import static com.zerogift.global.error.code.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.zerogift.global.error.code.ProductErrorCode.PRODUCT_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final GiftBoxRepository giftBoxRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    private static final double MILEAGE_ACCUMULATION_RATE = 0.05;

    @Override
    public ReviewResponse addReview(LoginInfo loginInfo, Long productId, ReviewInput reviewInput) {
        // 회원 정보 가져오기
        Member member = memberRepository.findByEmail(loginInfo.getEmail())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        // 상품 정보 가져오기
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

        // 같은 상품을 여러개 받았을경우 첫번째 상품 사용
        List<GiftBox> giftBoxList = giftBoxRepository.findByRecipientMemberAndProductAndReview(member, product, false);
        if (giftBoxList.isEmpty()) {
            throw new NotFoundGiftBoxException(GIFT_BOX_NOT_FOUND);
        }
        GiftBox giftBox = giftBoxList.get(0);

        // 해당 선물 사용 여부 체크
        if (Boolean.FALSE.equals(giftBox.getIsUse())) {
            throw new NotFoundGiftBoxException(GIFT_BOX_NOT_USE);
        }

        // 해당 선물에 리뷰 유무 체크
        giftBox.review(true);

        // 리뷰 내용 저장
        Review review = reviewRepository.save(Review.builder()
                .rank(reviewInput.getRank())
                .description(reviewInput.getDescription())
                .member(member)
                .product(product)
                .giftBox(giftBox)
                .build()
        );

        // 해당 선물의 리뷰 작성이 처음일 경우만 포인트 적립 (최대 1000point)
        if (!pointHistoryRepository.existsByMemberAndPointTypeAndPointTypeId(member, PointType.REVIEW, review.getGiftBox().getId())) {
            int point = (int) (giftBox.getPayHistory().getPrice() * MILEAGE_ACCUMULATION_RATE);
            int mileagePoint = Math.min(point, 1000);
            // 회원 정보에 포인트 저장
            member.mileagePoints(mileagePoint);
            // 포인트 저장 기록하기
            pointHistoryRepository.save(PointHistory.builder()
                    .member(member)
                    .point(point)
                    .pointType(PointType.REVIEW)
                    .pointTypeId(review.getGiftBox().getId())
                    .build());
        }

        // sse 보내기
        applicationEventPublisher.publishEvent(new EventInfo(review.getMember(),
                review.getProduct().getMember(), NoticeType.review, review.getId()));

        // member 와 product 내용 편집해서 출력
        return ReviewResponse.from(review);
    }

    @Override
    public ReviewResponse modifyReview(LoginInfo loginInfo, Long reviewId, ReviewInput reviewInput) {
        // 수정할 리뷰 가져오기
        Review review = reviewRepository.findReviewByMember(loginInfo.getEmail(), reviewId)
                .orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));

        // 리뷰 별점과 내용 수정
        review.modify(reviewInput.getRank(), reviewInput.getDescription());

        // member 와 product 내용 편집해서 출력
        return ReviewResponse.from(review);
    }

    @Override
    public void removeReview(LoginInfo loginInfo, Long reviewId) {
        // 삭제할 리뷰 가져오기
        Review review = reviewRepository.findReviewByMember(loginInfo.getEmail(), reviewId)
                .orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));

        // 해당 리뷰 삭제
        reviewRepository.delete(review);

        // 리뷰 삭제 된 선물의 review 유무 체크 해제
        GiftBox giftBox = giftBoxRepository.findById(review.getGiftBox().getId())
                .orElseThrow(() -> new NotFoundGiftBoxException(GIFT_BOX_NOT_FOUND));
        giftBox.review(false);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReviewResponse> userReviewList(LoginInfo loginInfo) {
        // 회원 email 로 review 리스트 가져오기
        return reviewRepository.findReviewListByMember(loginInfo.getEmail())
                .stream().map(ReviewResponse::listFrom).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReviewResponse> productReviewList(Long productId) {
        // 상품 ID 로 review 리스트 가져오기
        return reviewRepository.findReviewListByProduct(productId)
                .stream().map(ReviewResponse::listFrom).collect(Collectors.toList());
    }
}
