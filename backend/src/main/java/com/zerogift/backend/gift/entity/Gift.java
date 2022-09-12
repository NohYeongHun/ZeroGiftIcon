package com.zerogift.backend.gift.entity;

import com.zerogift.backend.common.entity.BaseTimeEntity;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.product.entity.Product;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Gift extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean isUse;

    @Column(nullable = false)
    private String code; // 사용 코드

    @Column(nullable = false)
    private LocalDate expired_date;

    @Column(nullable = false)
    private Boolean answer;  // 답변을 하였는지

    @ManyToOne
    @JoinColumn(name = "send_member_id")
    private Member sendMember;

    @ManyToOne
    @JoinColumn(name = "recipient_member_id")
    private Member recipientMember;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
