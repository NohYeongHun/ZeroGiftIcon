package com.zerogift.member.application;

import com.zerogift.global.error.code.MemberErrorCode;
import com.zerogift.global.error.exception.MemberException;
import com.zerogift.member.domain.Member;
import com.zerogift.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeneralService {

    private final MemberRepository memberRepository;

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

}
