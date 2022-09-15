package com.zerogift.backend.likes.model;

import com.zerogift.backend.likes.entity.Likes;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.product.entity.Product;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class LikesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public static LikesModel of(Likes likes) {
        return LikesModel.builder()
                .member(likes.getMember())
                .product(likes.getProduct())
                .build();
    }
}
