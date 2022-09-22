package com.zerogift.backend.member.service;

import com.zerogift.backend.common.exception.EmailAuthException;
import com.zerogift.backend.common.exception.code.EmailAuthErrorCode;
import com.zerogift.backend.common.exception.code.MemberErrorCode;
import com.zerogift.backend.common.exception.member.MemberException;
import com.zerogift.backend.common.type.AuthType;
import com.zerogift.backend.email.dto.EmailAuthRequest;
import com.zerogift.backend.email.entity.EmailAuth;
import com.zerogift.backend.email.repository.EmailAuthRepository;
import com.zerogift.backend.email.service.EmailService;
import com.zerogift.backend.member.dto.MemberLoginRequest;
import com.zerogift.backend.member.dto.MemberRegisterRequest;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.member.repository.MemberRepository;
import com.zerogift.backend.member.type.MemberStatus;
import com.zerogift.backend.security.dto.MemberInfo;
import com.zerogift.backend.security.dto.TokenDto;
import com.zerogift.backend.security.repository.RefreshTokenRepository;
import com.zerogift.backend.security.service.TokenService;
import com.zerogift.backend.security.type.Role;
import lombok.RequiredArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberLoginService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailAuthRepository emailAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final EmailService emailService;

    @Transactional
    public TokenDto login(MemberLoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        validateLoginMember(member, request.getPassword());

        return tokenService.issueAllToken(
                MemberInfo.of(
                        MemberInfo.builder()
                                .email(member.getEmail())
                                .role(member.getRole().name())
                                .build().toClaims()
                ));
    }

    private void validateLoginMember(Member member, String password) {
        if (!passwordEncoder.matches(password, member.getPassword())){
            throw new MemberException(MemberErrorCode.PASSWORD_WRONG);
        }

        if (member.getStatus().equals(MemberStatus.WAIT)){
            emailService.sendEmail(member.getEmail());
            throw new MemberException(MemberErrorCode.EMAIL_VERIFICATION_HAS_NOT_BEEN_COMPLETED);
        }

        if (member.getStatus().equals(MemberStatus.BANNED)){
            throw new MemberException(MemberErrorCode.SUSPEND_USER);
        }

        if (member.getStatus().equals(MemberStatus.WITHDRAWAL)){
            throw new MemberException(MemberErrorCode.WITHDRAW_USER);
        }

    }

    @Transactional
    public Member registerNewMember(MemberRegisterRequest request){

        validateRegisterMember(request.getEmail(), request.getNickname());

        Member member = Member.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .status(MemberStatus.WAIT)
                .authType(AuthType.GENERAL)
                .role(Role.ROLE_MEMBER)
                .build();
        String test = passwordEncoder.encode(request.getPassword());
        member.addPassword(test);
        memberRepository.save(member);
        emailService.sendEmail(request.getEmail());

        return member;


    }

    private void validateRegisterMember(String email, String nickname){

        if(memberRepository.findByEmail(email).isPresent()){
            throw new MemberException(MemberErrorCode.SAME_EMAIL_EXISTS);
        }

        if(memberRepository.findByNickname(nickname).isPresent()){
            throw new MemberException(MemberErrorCode.SAME_NICKNAME_EXISTS);
        }
    }

    public void logoutMember(MemberInfo memberInfo) {
        refreshTokenRepository.deleteByUsername(memberInfo.getEmail());
    }



    @Transactional
    public void confirmEmail(String email, String authToken) {
        EmailAuth emailAuth = emailAuthRepository.findValidAuthByEmail(
                        email,
                        authToken,
                        LocalDateTime.now()
                )
                .orElseThrow(() -> new EmailAuthException(EmailAuthErrorCode.AUTH_TOKEN_NOT_FOUND));
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        emailAuth.useToken();
        member.emailVerifiedSuccess();
    }


}
