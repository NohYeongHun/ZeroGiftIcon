package com.zerogift.gift.repository;

import static com.zerogift.gift.domain.QGiftBox.giftBox;
import static com.zerogift.gift.domain.QGiftMessage.giftMessage;
import static com.zerogift.member.domain.QMember.member;
import static com.zerogift.product.domain.QProduct.product;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerogift.gift.application.dto.GiftMessageDto;
import com.zerogift.gift.application.dto.GiftMessageForm;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GiftMessageRepositoryCustomImpl implements GiftMessageRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public GiftMessageForm findByGiftMessageForm(Long giftBoxId, Long memberId) {
        return queryFactory
            .select(Projections.constructor(GiftMessageForm.class,
                member.nickname,
                product.mainImageUrl
            ))
            .from(giftBox)
            .innerJoin(giftBox.product, product)
            .innerJoin(giftBox.recipientMember, member)
            .where(eqGiftBoxId(giftBoxId)
                , eqMemberId(memberId))
            .fetchOne();
    }

    private BooleanExpression eqGiftBoxId(Long giftBoxId) {
        return giftBox.id.eq(giftBoxId);
    }

    private BooleanExpression eqMemberId(Long memberId) {
        return member.id.eq(memberId);
    }

    @Override
    public Optional<GiftMessageDto> findByGiftMessage(Long giftMessageId, Long memberId) {
        return Optional.ofNullable(queryFactory
            .select(Projections.constructor(GiftMessageDto.class,
                product.mainImageUrl,
                member.nickname,
                giftMessage.message
            ))
            .from(giftMessage)
            .innerJoin(giftMessage.product, product)
            .innerJoin(giftMessage.fromMember, member)
            .where(giftMessage.id.eq(giftMessageId)
                .and(member.id.eq(memberId)))
            .fetchOne());
    }
}
