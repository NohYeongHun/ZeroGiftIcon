package com.zerogift.backend.notice.model;

import com.zerogift.backend.notice.type.NoticeType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReviewNoticeResponse {

    private Long reviewId;

    private String message;

    private Long toMemberId;

    private Long fromMemberId;

    private NoticeType noticeType;

    @Builder
    public ReviewNoticeResponse(Long reviewId, Long toMemberId,
                                Long fromMemberId, String message) {
        this.reviewId = reviewId;
        this.message = message;
        this.toMemberId = toMemberId;
        this.fromMemberId = fromMemberId;
        this.noticeType = NoticeType.review;
    }

}
