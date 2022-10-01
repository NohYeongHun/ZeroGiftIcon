package com.zerogift.backend.notice.model;

import com.zerogift.backend.notice.entity.Notice;
import com.zerogift.backend.notice.type.NoticeType;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Builder
public class NoticeResponse {

    private NoticeType noticeType;

    private Long noticeTypeId;

    private Long fromMemberId;

    private String message;

    private LocalDateTime createdDate;

    public NoticeResponse(NoticeType noticeType, Long noticeTypeId, Long fromMemberId,
                          String message, LocalDateTime createdDate) {
        this.noticeType = noticeType;
        this.noticeTypeId = noticeTypeId;
        this.fromMemberId = fromMemberId;
        this.message = message;
        this.createdDate = createdDate;
    }

    public static NoticeResponse from(Notice notice) {
        return NoticeResponse.builder()
                    .noticeType(notice.getNoticeType())
                    .noticeTypeId(notice.getNoticeTypeId())
                    .fromMemberId(notice.getFromMember().getId())
                    .message(notice.getMessage())
                    .createdDate(notice.getCreatedDate())
                .build();
    }

}
