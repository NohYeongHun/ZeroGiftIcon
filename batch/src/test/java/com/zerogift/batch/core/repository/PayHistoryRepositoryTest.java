package com.zerogift.batch.core.repository;

import static com.zerogift.batch.data.MakeDataUtils.createMember;
import static com.zerogift.batch.data.MakeDataUtils.createPayHistroy;
import static com.zerogift.batch.data.MakeDataUtils.createProduct;
import static com.zerogift.batch.data.MakeDataUtils.createProductImage;
import static org.assertj.core.api.Assertions.assertThat;

import com.zerogift.batch.core.dto.SaleStatisticsDto;
import com.zerogift.batch.core.entity.member.Member;
import com.zerogift.batch.core.entity.product.Product;
import com.zerogift.batch.core.entity.product.ProductImage;
import com.zerogift.batch.core.repository.member.MemberRepository;
import com.zerogift.batch.core.repository.pay.PayHistoryRepository;
import com.zerogift.batch.core.repository.product.ProductImageRepository;
import com.zerogift.batch.core.repository.product.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PayHistoryRepositoryTest {

    @Autowired
    private PayHistoryRepository payHistoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    private Member member;
    private Product product;
    private ProductImage productImage;

    @BeforeEach
    void setup() {
        member = createMember();
        memberRepository.save(member);

        product = createProduct(member);
        productRepository.save(product);

        productImage = createProductImage(product);
        productImageRepository.save(productImage);

        payHistoryRepository.save(createPayHistroy(product, member));
        payHistoryRepository.save(createPayHistroy(product, member));
    }

    @DisplayName("판매자 통계 리스트 반환")
    @Test
    void findBySeller() {
        List<SaleStatisticsDto> saleStatisticsDtos = payHistoryRepository.findStatisticBySeller(
            member);

        SaleStatisticsDto extract = SaleStatisticsDto.builder()
            .thumbnail(productImage.getUrl())
            .productName(product.getName())
            .count(2L)
            .price(2000L)
            .build();

        assertThat(saleStatisticsDtos).containsExactly(extract);
    }

}
