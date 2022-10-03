package com.zerogift.pay.domain;

import com.zerogift.support.domain.BaseTimeEntity;
import com.zerogift.member.domain.Member;
import com.zerogift.product.domain.Product;
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
import org.hibernate.annotations.CreationTimestamp;

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

    @Column(nullable = false)
    private Integer usePoint;

    @CreationTimestamp
    private LocalDateTime payDate;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member fromMember;

    @ManyToOne
    @JoinColumn(name = "to_member_id")
    private Member toMember;

    @Builder
    public PayHistory(String impUid, String merchantUid, String name, Integer price,
        String pgProvider, String pgTid, Integer usePoint, Product product, Member fromMember,
        Member toMember) {
        this.impUid = impUid;
        this.merchantUid = merchantUid;
        this.name = name;
        this.price = price;
        this.pgProvider = pgProvider;
        this.pgTid = pgTid;
        this.usePoint = usePoint;
        this.product = product;
        this.fromMember = fromMember;
        this.toMember = toMember;
    }

}
