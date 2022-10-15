package com.zerogift.product.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerogift.product.application.dto.ReviewQueryModel;
import com.zerogift.product.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.zerogift.member.domain.QMember.member;
import static com.zerogift.product.domain.QProduct.product;
import static com.zerogift.product.domain.QReview.review;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Review> findReviewByMember(String email, Long reviewId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(review)
                .innerJoin(review.member, member)
                .where(member.email.eq(email)
                        ,review.id.eq(reviewId))
                .fetchOne());
    }

    @Override
    public List<ReviewQueryModel> findReviewListByMember(String email) {
        return queryFactory
                .select(Projections.constructor(ReviewQueryModel.class,
                        review.id,
                        review.rank,
                        review.description,
                        review.member,
                        review.product,
                        review.createdDate,
                        review.lastModifiedDate
                ))
                .from(review)
                .innerJoin(review.member, member)
                .where(member.email.eq(email))
                .fetch();
    }

    @Override
    public List<ReviewQueryModel> findReviewListByProduct(Long productId) {
        return queryFactory
                .select(Projections.constructor(ReviewQueryModel.class,
                        review.id,
                        review.rank,
                        review.description,
                        review.member,
                        review.product,
                        review.createdDate,
                        review.lastModifiedDate
                ))
                .from(review)
                .innerJoin(review.product, product)
                .where(product.id.eq(productId))
                .fetch();
    }
}
