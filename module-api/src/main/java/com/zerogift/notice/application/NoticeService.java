package com.zerogift.notice.application;

import com.zerogift.global.error.code.MemberErrorCode;
import com.zerogift.global.error.code.NoticeErrorCode;
import com.zerogift.global.error.exception.MemberException;
import com.zerogift.global.error.exception.NoticeException;
import com.zerogift.member.domain.Member;
import com.zerogift.member.repository.MemberRepository;
import com.zerogift.notice.application.dto.EventInfo;
import com.zerogift.notice.application.dto.NoticeResponse;
import com.zerogift.notice.domain.Notice;
import com.zerogift.notice.repository.NoticeRepository;
import com.zerogift.support.auth.userdetails.LoginInfo;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@Service
@Slf4j
public class NoticeService {

    private static final Long TIMEOUT = 60 * 60 * 60 * 1000L;

    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    private Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    public SseEmitter createSseEmitter(LoginInfo loginInfo) {

        Long memberId = memberRepository.findByEmail(loginInfo.getEmail())
            .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND)).getId();

        SseEmitter sseEmitter = new SseEmitter(TIMEOUT);

        sseEmitter.onCompletion(() -> sseEmitters.remove(memberId));
        sseEmitter.onTimeout(() -> sseEmitters.remove(memberId));
        sseEmitter.onError((e) -> sseEmitters.remove(memberId));

        sseEmitters.put(memberId, sseEmitter);

        try {
            sseEmitter.send(SseEmitter.event()
                .id(memberId + "_" + System.currentTimeMillis())
                .name("sse")
                .data("connect"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sseEmitter;
    }

    @Async
    @EventListener
    public void sendEvent(final EventInfo eventInfo) {
        Long userId = eventInfo.getToMember().getId();
        SseEmitter sseEmitter = sseEmitters.get(userId);

        Notice notice = Notice.builder()
            .fromMember(eventInfo.getFromMember())
            .toMember(eventInfo.getToMember())
            .message(eventInfo.getFromMember().getNickname() + "님께서 " + eventInfo.getNoticeType()
                .getDescription())
            .noticeType(eventInfo.getNoticeType())
            .noticeTypeId(eventInfo.getId())
            .build();
        noticeRepository.save(notice);

        if (!Objects.isNull(sseEmitter)) {
            sendToClient(sseEmitter, userId);
        }
    }

    private void sendToClient(SseEmitter sseEmitter, Long memberId) {
        try {
            sseEmitter.send(SseEmitter.event()
                .id(memberId + "_" + System.currentTimeMillis())
                .name("sse")
                .data("알림도착", MediaType.APPLICATION_JSON)
                .reconnectTime(0));

            sseEmitter.complete();
        } catch (Exception exception) {
            sseEmitter.completeWithError(exception);
            sseEmitters.remove(memberId);
            exception.printStackTrace();
        }
    }

    @Transactional
    public void checkNotice(LoginInfo loginInfo, Long noticeId) {
        Member member = memberRepository.findByEmail(loginInfo.getEmail()).
            orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        Notice notice = noticeRepository.findByIdAndToMember(noticeId, member)
            .orElseThrow(() -> new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND));
        notice.checkView();
    }

    public List<NoticeResponse> noticeList(LoginInfo loginInfo) {
        Member member = memberRepository.findByEmail(loginInfo.getEmail()).
            orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        return noticeRepository.findByNoticeList(member, true);
    }

    public List<NoticeResponse> uncheckedNoticeList(LoginInfo loginInfo) {
        Member member = memberRepository.findByEmail(loginInfo.getEmail()).
            orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        return noticeRepository.findByNoticeList(member, false);
    }
}
