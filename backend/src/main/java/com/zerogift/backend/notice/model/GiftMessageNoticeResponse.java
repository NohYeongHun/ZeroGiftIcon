package com.zerogift.backend.notice.model;

import com.zerogift.backend.notice.type.NoticeType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GiftMessageNoticeResponse {

    private Long giftMessageId;

    private String message;

    private Long toMemberId;

    private Long fromMemberId;

    private NoticeType noticeType;

    @Builder
    public GiftMessageNoticeResponse(Long giftMessageId, Long toMemberId,
                                     Long fromMemberId, String message) {
        this.giftMessageId = giftMessageId;
        this.message = message;
        this.toMemberId = toMemberId;
        this.fromMemberId = fromMemberId;
        this.noticeType = NoticeType.message;
    }

}
