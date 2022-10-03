package com.zerogift.notice.application.dto;

import com.zerogift.notice.domain.Notice;
import com.zerogift.notice.domain.NoticeType;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class NoticeResponse {

    private NoticeType noticeType;

    private Long noticeTypeId;

    private String fromMemberNickName;

    private String message;

    private LocalDateTime createdDate;

    public NoticeResponse(NoticeType noticeType, Long noticeTypeId,
        String fromMemberNickName, String message, LocalDateTime createdDate) {
        this.noticeType = noticeType;
        this.noticeTypeId = noticeTypeId;
        this.fromMemberNickName = fromMemberNickName;
        this.message = message;
        this.createdDate = createdDate;
    }

}
