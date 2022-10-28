package com.zerogift.member.application;

import com.zerogift.email.domain.EmailAuth;
import com.zerogift.email.infrastructure.EmailService;
import com.zerogift.email.repository.EmailAuthRepository;
import com.zerogift.global.error.code.EmailAuthErrorCode;
import com.zerogift.global.error.code.MemberErrorCode;
import com.zerogift.global.error.exception.EmailAuthException;
import com.zerogift.global.error.exception.MemberException;
import com.zerogift.member.application.dto.MemberLoginRequest;
import com.zerogift.member.application.dto.MemberRegisterRequest;
import com.zerogift.member.application.dto.MemberRegisterInfo;
import com.zerogift.member.domain.AuthType;
import com.zerogift.member.domain.Member;
import com.zerogift.member.domain.MemberStatus;
import com.zerogift.member.domain.Role;
import com.zerogift.member.repository.MemberRepository;
import com.zerogift.support.auth.oauth.application.TokenService;
import com.zerogift.support.auth.oauth.infrastructure.RefreshTokenRepository;
import com.zerogift.support.auth.token.TokenDto;
import com.zerogift.support.auth.userdetails.MemberInfo;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                    .id(member.getId())
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
    public MemberRegisterInfo registerNewMember(MemberRegisterRequest request){

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

        return MemberRegisterInfo.from(member);


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
