package com.zerogift.pay.repository;

import static com.zerogift.member.domain.QMember.member;
import static com.zerogift.pay.domain.QPayHistory.payHistory;
import static com.zerogift.product.domain.QProduct.product;
import static com.zerogift.product.domain.QProductImage.productImage;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerogift.member.domain.Member;
import com.zerogift.pay.application.dto.SaleStatisticsDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PayHistoryRepositoryCustomerImpl implements PayHistoryRepositoryCustomer {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SaleStatisticsDto> findStatisticBySeller(Member seller) {
        return queryFactory
            .select(Projections.constructor( SaleStatisticsDto.class,
                productImage.url,
                payHistory.name,
                payHistory.id.count(),
                payHistory.price.sum().longValue()
            ))
            .from(payHistory)
            .innerJoin(payHistory.product, product)
            .innerJoin(productImage)
            .on(productImage.product.eq(product)
                .and(productImage.isMainImage.eq(true)))
            .innerJoin(payHistory.product, product)
            .innerJoin(product.member, member)
            .where(member.eq(seller))
            .groupBy(payHistory.toMember,
                product.id,
                product.name,
                productImage.url,
                payHistory.price)
            .fetch();
    }
}
