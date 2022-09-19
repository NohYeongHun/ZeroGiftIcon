package com.zerogift.backend.giftMessage.entity;

import com.zerogift.backend.common.entity.BaseTimeEntity;
import com.zerogift.backend.giftBox.entity.GiftBox;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.product.entity.Product;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class GiftMessage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @ManyToOne
    @JoinColumn(name = "to_member_id")
    private Member toMember;

    @ManyToOne
    @JoinColumn(name = "from_member_id")
    private Member fromMember;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne
    @JoinColumn(name = "giftBox_id")
    private GiftBox giftBox;

    @Builder
    public GiftMessage(String message, Member toMember,
        Member fromMember, Product product, GiftBox giftBox) {
        this.message = message;
        this.toMember = toMember;
        this.fromMember = fromMember;
        this.product = product;
        this.giftBox = giftBox;
    }
}
