package com.zerogift.backend.notice.service;

import com.zerogift.backend.common.exception.code.NoticeErrorCode;
import com.zerogift.backend.common.exception.notice.NoticeException;
import com.zerogift.backend.giftBox.entity.GiftBox;
import com.zerogift.backend.giftMessage.entity.GiftMessage;
import com.zerogift.backend.notice.entity.Notice;
import com.zerogift.backend.notice.model.GiftNoticeResponse;
import com.zerogift.backend.notice.model.GiftMessageNoticeResponse;
import com.zerogift.backend.notice.model.ReviewNoticeResponse;
import com.zerogift.backend.notice.repository.NoticeRepository;
import com.zerogift.backend.notice.type.NoticeType;
import com.zerogift.backend.review.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.transaction.Transactional;
import java.io.IOException;

import static com.zerogift.backend.notice.controller.NoticeController.sseEmitters;

@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Async
    public void sendReviewEvent(Review review) {

        Long userId = review.getProduct().getMember().getId();

        SseEmitter sseEmitter = sseEmitters.get(userId);

        // notice 저장
        Notice notice = Notice.builder()
                .fromMember(review.getMember())
                .toMember(review.getProduct().getMember())
                .message(review.getMember().getNickname() + "님께서 리뷰를 등록하셨습니다.")
                .noticeType(NoticeType.review)
                .noticeTypeId(review.getId())
                .build();
        noticeRepository.save(notice);

        try {
            sseEmitter.send(SseEmitter
                    .event()
                    .name("알림이 생성되었습니다.")
                    .data(ReviewNoticeResponse.builder()
                            .reviewId(notice.getNoticeTypeId())
                            .message(notice.getMessage())
                            .toMemberId(notice.getToMember().getId())
                            .fromMemberId(notice.getFromMember().getId())
                    )
            );
        } catch (IOException e) {
            sseEmitters.remove(userId);
        }
    }

    @Async
    public void sendGiftEvent(GiftBox giftBox) {

        Long userId = giftBox.getProduct().getMember().getId();

        SseEmitter sseEmitter = sseEmitters.get(userId);

        // notice 저장
        Notice notice = Notice.builder()
                .fromMember(giftBox.getSendMember())
                .toMember(giftBox.getRecipientMember())
                .message(giftBox.getSendMember().getNickname() + "님께서 선물하셨습니다.")
                .noticeType(NoticeType.gift)
                .noticeTypeId(giftBox.getId())
                .build();
        noticeRepository.save(notice);

        try {
            sseEmitter.send(SseEmitter
                    .event()
                    .name("알림이 생성되었습니다.")
                    .data(GiftNoticeResponse.builder()
                            .giftBoxId(notice.getNoticeTypeId())
                            .message(notice.getMessage())
                            .toMemberId(notice.getToMember().getId())
                            .fromMemberId(notice.getFromMember().getId())
                    )
            );
        } catch (IOException e) {
            sseEmitters.remove(userId);
        }
    }

    @Async
    public void sendMessageEvent(GiftMessage giftMessage) {

        Long userId = giftMessage.getProduct().getMember().getId();

        SseEmitter sseEmitter = sseEmitters.get(userId);

        // notice 저장
        Notice notice = Notice.builder()
                .fromMember(giftMessage.getFromMember())
                .toMember(giftMessage.getProduct().getMember())
                .message(giftMessage.getFromMember() + "님께서 감사메시지를 보내셨습니다.")
                .noticeType(NoticeType.review)
                .noticeTypeId(giftMessage.getId())
                .build();
        noticeRepository.save(notice);

        try {
            sseEmitter.send(SseEmitter
                    .event()
                    .name("알림이 생성되었습니다.")
                    .data(GiftMessageNoticeResponse.builder()
                            .giftMessageId(notice.getNoticeTypeId())
                            .message(notice.getMessage())
                            .toMemberId(notice.getToMember().getId())
                            .fromMemberId(notice.getFromMember().getId())
                    )
            );
        } catch (IOException e) {
            sseEmitters.remove(userId);
        }
    }

    @Transactional
    public void checkNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND));
        notice.checkView();
    }
}
