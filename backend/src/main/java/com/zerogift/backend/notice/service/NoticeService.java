package com.zerogift.backend.notice.service;

import com.zerogift.backend.common.exception.code.NoticeErrorCode;
import com.zerogift.backend.common.exception.notice.NoticeException;
import com.zerogift.backend.notice.entity.Notice;
import com.zerogift.backend.notice.repository.NoticeRepository;
import com.zerogift.backend.review.entity.Review;
import com.zerogift.backend.security.dto.LoginInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.transaction.Transactional;
import java.io.IOException;

import static com.zerogift.backend.notice.controller.NoticeController.sseEmitters;

@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public void addReviewEvent(Review review) {

        Long userId = review.getProduct().getMember().getId();

        if (sseEmitters.containsKey(userId)) {
            SseEmitter sseEmitter = sseEmitters.get(userId);

            Notice notice = Notice.builder()
                    .fromMember(review.getMember())
                    .toMember(review.getProduct().getMember())
                    .message(review.getDescription())
                    .build();
            noticeRepository.save(notice);

            try {
                sseEmitter.send(SseEmitter
                        .event()
                        .name("새로운 리뷰 등록")
                        .data(notice));
            } catch (IOException e) {
                sseEmitters.remove(userId);
            }
        }
    }

    @Transactional
    public void checkNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND));
        notice.checkView();
    }
}
