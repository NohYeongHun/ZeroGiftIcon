package com.zerogift.backend.notice.model;

import com.zerogift.backend.notice.type.NoticeType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GiftNoticeResponse {

    private Long giftBoxId;

    private String message;

    private Long toMemberId;

    private Long fromMemberId;

    private NoticeType noticeType;

    @Builder
    public GiftNoticeResponse(Long giftBoxId, Long toMemberId,
                              Long fromMemberId, String message) {
        this.giftBoxId = giftBoxId;
        this.message = message;
        this.toMemberId = toMemberId;
        this.fromMemberId = fromMemberId;
        this.noticeType = NoticeType.gift;
    }

}
