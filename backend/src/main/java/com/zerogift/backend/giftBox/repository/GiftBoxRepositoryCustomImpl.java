package com.zerogift.backend.giftBox.repository;

import static com.zerogift.backend.giftBox.entity.QGiftBox.giftBox;
import static com.zerogift.backend.giftMessage.entity.QGiftMessage.giftMessage;
import static com.zerogift.backend.member.entity.QMember.member;
import static com.zerogift.backend.product.entity.QProduct.product;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerogift.backend.common.dto.MyPageableDto;
import com.zerogift.backend.giftBox.dto.GiftBoxDetail;
import com.zerogift.backend.giftBox.dto.GiftBoxDto;
import com.zerogift.backend.giftBox.entity.GiftBox;
import com.zerogift.backend.giftMessage.entity.QGiftMessage;
import com.zerogift.backend.member.entity.Member;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;



@Repository
@RequiredArgsConstructor
public class GiftBoxRepositoryCustomImpl implements GiftBoxRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<GiftBoxDto> findByIsUseEqFalse(Member loginMember, MyPageableDto myPageableDto) {
        return queryFactory
            .select(Projections.constructor(GiftBoxDto.class,
                giftBox.id,
                product.name,
                product.mainImageUrl,
                product.description,
                giftBox.isUse,
                member.id,
                member.nickname,
                product.id,
                giftBox.answer,
                giftBox.review,
                giftMessage.id
            ))
            .from(giftBox)
            .innerJoin(giftBox.product, product)
            .innerJoin(giftBox.sendMember, member)
            .leftJoin(giftMessage)
                .on(giftMessage.giftBox.eq(giftBox))
                .fetchJoin()
            .where(
                eqRecipientMember(loginMember))
            .orderBy(giftBox.createdDate.asc())
            .offset(currentOffset(myPageableDto))
            .limit(nextOffset(myPageableDto))
            .fetch();
    }

    private BooleanExpression eqRecipientMember(Member member) {
        return giftBox.recipientMember.eq(member);
    }

    private long currentOffset(MyPageableDto myPageableDto) {
        return myPageableDto.getPage() * myPageableDto.getSize();
    }

    private long nextOffset(MyPageableDto myPageableDto) {
        return (myPageableDto.getPage() + 1) * myPageableDto.getSize();
    }

    @Override
    public GiftBoxDetail findByGiftBoxId(Long giftBoxId, Member member) {
        return queryFactory
            .select(Projections.constructor(GiftBoxDetail.class,
                product.name,
                product.mainImageUrl,
                giftBox.barcodeUrl,
                giftBox.answer,
                giftMessage.id
            ))
            .from(giftBox)
            .innerJoin(giftBox.product, product)
            .leftJoin(giftMessage)
                .on(giftMessage.giftBox.eq(giftBox))
            .fetchJoin()
            .where(giftBox.id.eq(giftBoxId)
                .and(giftBox.recipientMember.eq(member)))
            .fetchOne();
    }

    @Override
    public Optional<GiftBox> findGiftBoxById(Long giftBoxId) {
        return Optional.ofNullable(queryFactory
            .selectFrom(giftBox)
            .innerJoin(giftBox.product, product)
            .innerJoin(giftBox.sendMember, member)
            .where(giftBox.id.eq(giftBoxId))
            .fetchOne());
    }


}
