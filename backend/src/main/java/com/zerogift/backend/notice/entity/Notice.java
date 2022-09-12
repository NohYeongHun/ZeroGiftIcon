package com.zerogift.backend.notice.entity;

import com.zerogift.backend.common.entity.BaseTimeEntity;
import com.zerogift.backend.member.entity.Member;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Notice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private Boolean isView;

    @ManyToOne
    @JoinColumn(name = "to_member_id")
    private Member toMember;

    @ManyToOne
    @JoinColumn(name = "from_member_id")
    private Member fromMember;

}
