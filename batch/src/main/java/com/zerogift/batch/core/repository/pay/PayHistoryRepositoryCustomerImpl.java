package com.zerogift.batch.core.repository.pay;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerogift.batch.core.dto.SaleStatisticsDto;
import com.zerogift.batch.core.entity.member.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.zerogift.batch.core.entity.pay.QPayHistory.payHistory;
import static com.zerogift.batch.core.entity.product.QProduct.product;
import static com.zerogift.batch.core.entity.product.QProductImage.productImage;

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
                    .and(productImage.thumbnail.eq(true)))
            .where(payHistory.seller.eq(seller))
            .groupBy(payHistory.toMember,
                product.id,
                product.name,
                productImage.url,
                payHistory.price)
            .fetch();
    }
}
