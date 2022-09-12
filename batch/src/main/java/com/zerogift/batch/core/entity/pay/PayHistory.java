package com.zerogift.batch.core.entity.pay;

import com.zerogift.batch.core.entity.base.BaseTimeEntity;
import com.zerogift.batch.core.entity.member.Member;
import com.zerogift.batch.core.entity.product.Product;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PayHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "imp_uid", nullable = false)
    private String impUid;

    @Column(name = "merchat_uid", nullable = false)
    private String merchantUid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "pg_provider", nullable = false)
    private String pgProvider;

    @Column(name = "pg_tid", nullable = false)
    private String pgTid;

    private LocalDateTime payDate;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member seller;

    @ManyToOne
    @JoinColumn(name = "to_member_id")
    private Member toMember;

    @Builder
    public PayHistory(String impUid, String merchantUid, String name, Integer price,
        String pgProvider, String pgTid, LocalDateTime payDate,
        Product product, Member seller, Member toMember) {
        this.impUid = impUid;
        this.merchantUid = merchantUid;
        this.name = name;
        this.price = price;
        this.pgProvider = pgProvider;
        this.pgTid = pgTid;
        this.payDate = payDate;
        this.product = product;
        this.seller = seller;
        this.toMember = toMember;
    }
}
