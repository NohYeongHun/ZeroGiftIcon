package com.zerogift.backend.notice.service;

import com.zerogift.backend.common.exception.code.MemberErrorCode;
import com.zerogift.backend.common.exception.code.NoticeErrorCode;
import com.zerogift.backend.common.exception.member.MemberException;
import com.zerogift.backend.common.exception.notice.NoticeException;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.member.repository.MemberRepository;
import com.zerogift.backend.notice.entity.Notice;
import com.zerogift.backend.notice.model.NoticeResponse;
import com.zerogift.backend.notice.model.NoticeTypeResponse;
import com.zerogift.backend.notice.repository.NoticeRepository;
import com.zerogift.backend.notice.type.NoticeType;
import com.zerogift.backend.security.dto.LoginInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.zerogift.backend.notice.controller.NoticeController.sseEmitters;

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
