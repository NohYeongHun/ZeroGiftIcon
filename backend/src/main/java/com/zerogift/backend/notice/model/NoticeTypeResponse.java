package com.zerogift.backend.notice.model;

import com.zerogift.backend.notice.type.NoticeType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
