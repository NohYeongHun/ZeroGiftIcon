package com.zerogift.backend.notice.entity;

import com.zerogift.backend.common.entity.BaseTimeEntity;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.notice.type.NoticeType;
import com.zerogift.backend.security.dto.LoginInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
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

    private NoticeType noticeType;

    // 해당 (리뷰, 감사메시지, 선물) id값
    private Long noticeTypeId;

    @Builder
    public Notice(Member fromMember, Member toMember, String message, NoticeType noticeType, Long noticeTypeId) {
        this.message = message;
        this.isView = false;
        this.toMember = toMember;
        this.fromMember = fromMember;
        this.noticeType = noticeType;
        this.noticeTypeId = noticeTypeId;
    }

    public void checkView() {
        this.isView = true;
    }
}
