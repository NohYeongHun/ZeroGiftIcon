package com.zerogift.notice.application;

import static com.zerogift.notice.presentation.NoticeController.sseEmitters;

import com.zerogift.global.error.code.MemberErrorCode;
import com.zerogift.global.error.code.NoticeErrorCode;
import com.zerogift.global.error.exception.MemberException;
import com.zerogift.global.error.exception.NoticeException;
import com.zerogift.member.domain.Member;
import com.zerogift.member.repository.MemberRepository;
import com.zerogift.notice.application.dto.NoticeResponse;
import com.zerogift.notice.application.dto.NoticeTypeResponse;
import com.zerogift.notice.domain.Notice;
import com.zerogift.notice.domain.NoticeType;
import com.zerogift.notice.repository.NoticeRepository;
import com.zerogift.support.auth.userdetails.LoginInfo;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    @Async
    public void sendEvent(Member fromMember, Member toMember, NoticeType noticeType, Long id) {

        Long userId = toMember.getId();
        SseEmitter sseEmitter = sseEmitters.get(userId);

        // notice 저장
        Notice notice = Notice.builder()
            .fromMember(fromMember)
            .toMember(toMember)
            .message(fromMember.getNickname() + "님께서 " + noticeType.getDescription())
            .noticeType(noticeType)
            .noticeTypeId(id)
            .build();
        noticeRepository.save(notice);

        if(!Objects.isNull(sseEmitter)) {
            try {
                sseEmitter.send(SseEmitter
                    .event()
                    .name("알림이 생성되었습니다.")
                    .data(NoticeTypeResponse.builder()
                        .noticeTypeId(notice.getNoticeTypeId())
                        .noticeType(noticeType)
                        .message(notice.getMessage())
                        .toMemberId(notice.getToMember().getId())
                        .fromMemberId(notice.getFromMember().getId())
                    )
                );
            } catch (IOException e) {
                sseEmitters.remove(userId);
            }
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

        return noticeRepository.findByToMember(member)
            .stream().map(x -> NoticeResponse.from(x)).collect(Collectors.toList());
    }

    public List<NoticeResponse> uncheckedNoticeList(LoginInfo loginInfo) {
        Member member = memberRepository.findByEmail(loginInfo.getEmail()).
            orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        return noticeRepository.findByToMemberAndIsView(member, false)
            .stream().map(x -> NoticeResponse.from(x)).collect(Collectors.toList());
    }
}
