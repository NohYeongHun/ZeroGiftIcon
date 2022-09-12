package com.zerogift.backend.pay.entity;

import com.zerogift.backend.common.entity.BaseTimeEntity;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.product.entity.Product;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
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
    private Member Member;

}
