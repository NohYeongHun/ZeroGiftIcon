package com.zerogift.notice.application.dto;

import com.zerogift.member.domain.Member;
import com.zerogift.notice.domain.NoticeType;
import lombok.Getter;

@Getter
public class EventInfo {

    private Member fromMember;
    private Member toMember;
    private NoticeType noticeType;
    private Long id;

    public EventInfo(Member fromMember, Member toMember,
        NoticeType noticeType, Long id) {
        this.fromMember = fromMember;
        this.toMember = toMember;
        this.noticeType = noticeType;
        this.id = id;
    }
}

