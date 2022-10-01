package com.zerogift.backend.review.entity;

import com.zerogift.backend.common.entity.BaseTimeEntity;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.product.entity.Product;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer rank;  // 별점 점수

    @Lob
    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public void modify(Integer rank, String description) {
        this.rank = rank;
        this.description = description;
    }

}
