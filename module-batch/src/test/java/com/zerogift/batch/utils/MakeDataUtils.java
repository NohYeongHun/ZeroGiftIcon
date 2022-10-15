package com.zerogift.batch.utils;

import com.zerogift.email.domain.EmailMessage;
import com.zerogift.email.domain.MessageStatus;
import com.zerogift.member.domain.Member;
import com.zerogift.member.domain.Role;
import com.zerogift.pay.domain.PayHistory;
import com.zerogift.product.domain.Category;
import com.zerogift.product.domain.Product;
import com.zerogift.product.domain.ProductImage;
import com.zerogift.product.domain.Status;

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
            .status(Status.PUBLIC)
            .member(member)
            .build();
    }

    public static ProductImage createProductImage(Product product) {
        return ProductImage.builder()
            .url("https://test.com/test.png")
            .isMainImage(true)
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
            .product(product)
            .toMember(member)
            .usePoint(0)
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
