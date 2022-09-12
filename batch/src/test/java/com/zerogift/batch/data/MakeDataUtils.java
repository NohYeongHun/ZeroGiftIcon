package com.zerogift.batch.data;

import com.zerogift.batch.core.entity.email.EmailMessage;
import com.zerogift.batch.core.entity.email.MessageStatus;
import com.zerogift.batch.core.entity.member.Member;
import com.zerogift.batch.core.entity.member.Role;
import com.zerogift.batch.core.entity.pay.PayHistory;
import com.zerogift.batch.core.entity.product.Category;
import com.zerogift.batch.core.entity.product.Product;
import com.zerogift.batch.core.entity.product.ProductImage;
import com.zerogift.batch.core.entity.product.ProductStatus;
import java.time.LocalDateTime;

public class MakeDataUtils {

    public static Member createMember() {
        return Member.builder()
            .email("test@zerobase.com")
            .nickname("2조")
            .role(Role.ROLE_MEMBER)
            .build();
    }

    public static Product createProduct(Member member) {
        return Product.builder()
            .name("테스트 상품")
            .description("테스트 상품 설명")
            .price(1000)
            .count(100)
            .category(Category.HEALTH)
            .status(ProductStatus.SELL)
            .member(member)
            .build();
    }

    public static ProductImage createProductImage(Product product) {
        return ProductImage.builder()
            .url("https://test.com/test.png")
            .thumbnail(true)
            .product(product)
            .build();
    }

    public static PayHistory createPayHistroy(Product product, Member member) {
        return PayHistory.builder()
            .impUid("test_impUid")
            .merchantUid("test_merchantUid")
            .name(product.getName())
            .price(product.getPrice())
            .pgProvider("test_pgProvider")
            .pgTid("test_pgTid")
            .payDate(LocalDateTime.now().minusMonths(1))
            .product(product)
            .seller(member)
            .toMember(member)
            .build();
    }

    public static EmailMessage createEmailMessage(Product product) {
        return EmailMessage.builder()
            .email(product.getMember().getEmail())
            .message("테스트 문자")
            .status(MessageStatus.GENERAL)
            .build();
    }

    public static EmailMessage createStaticsEmailMessage(Product product) {
        return EmailMessage.builder()
            .email(product.getMember().getEmail())
            .message("테스트 문자")
            .status(MessageStatus.STATISTIC)
            .build();
    }

}
