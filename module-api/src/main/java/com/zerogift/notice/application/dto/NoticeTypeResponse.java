package com.zerogift.notice.application.dto;

import com.zerogift.notice.domain.NoticeType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
public class NoticeTypeResponse {

    private Long noticeTypeId;

    private NoticeType noticeType;

    private String message;

    private Long toMemberId;

    private Long fromMemberId;

    @Builder
    public NoticeTypeResponse(Long noticeTypeId, NoticeType noticeType, String message,
        Long toMemberId, Long fromMemberId) {
        this.noticeTypeId = noticeTypeId;
        this.noticeType = noticeType;
        this.message = message;
        this.toMemberId = toMemberId;
        this.fromMemberId = fromMemberId;
    }

}
