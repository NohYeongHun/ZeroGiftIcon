package com.zerogift.backend.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nimbusds.oauth2.sdk.token.Tokens;
import com.zerogift.backend.common.exception.MemberException;
import com.zerogift.backend.common.exception.code.MemberErrorCode;
import com.zerogift.backend.common.type.AuthType;
import com.zerogift.backend.email.service.EmailService;
import com.zerogift.backend.member.dto.MemberLoginRequest;
import com.zerogift.backend.member.dto.MemberRegisterRequest;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.member.repository.MemberRepository;
import com.zerogift.backend.member.type.MemberStatus;
import com.zerogift.backend.security.dto.TokenDto;
import com.zerogift.backend.security.service.TokenService;
import com.zerogift.backend.security.type.Role;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MemberLoginServiceTest {

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    MemberRepository memberRepository;

    @Mock
    TokenService tokenService;

    @Mock
    EmailService emailService;

    @InjectMocks
    MemberLoginService memberLoginService;

    private Member member;
    private PasswordEncoder encoder;

    @BeforeEach
    void setup() {
        encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        member = Member.builder()
            .email("email@naver.com")
            .nickname("nickname")
            .status(MemberStatus.WAIT)
            .authType(AuthType.GENERAL)
            .role(Role.ROLE_MEMBER)
            .build();
    }

    @Test
    @DisplayName("회원 등록 - 성공")
    void testSuccessRegisterMember() {
        //given
        String password = "password";
        String encodingPassword = encoder.encode(password);
        MemberRegisterRequest request = MemberRegisterRequest.builder()
            .email(member.getEmail())
            .nickname(member.getNickname())
            .password(password)
            .build();

        member.addPassword(encodingPassword);
        given(memberRepository.findByEmail(anyString())).
            willReturn(Optional.empty());
        given(memberRepository.findByNickname(anyString()))
            .willReturn(Optional.empty());
        given(memberRepository.save(any()))
            .willReturn(member);
        when(passwordEncoder.encode(any())).thenReturn(encodingPassword);

        doNothing().when(emailService).sendEmail(anyString());

        //when
        Member registerMember = memberLoginService.registerNewMember(request);

        //then
        assertTrue(encoder.matches(
            password,
            registerMember.getPassword()
        ));
        assertEquals(member.getEmail(), registerMember.getEmail());
        assertEquals(member.getNickname(), registerMember.getNickname());
    }

    @Test
    @DisplayName("회원 가입 실패 - Email 존재")
    void testFailedRegisterMember_ExistsEmail(){
        //given
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member));

        String password = "password";
        MemberRegisterRequest request = MemberRegisterRequest.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .password(password)
                .build();
        //when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberLoginService.registerNewMember(request)
        );

        //then
        assertEquals(memberException.getErrorCode(), MemberErrorCode.SAME_EMAIL_EXISTS);
    }

    @Test
    @DisplayName("회원 가입 실패 - NickName 존재")
    void testFailedRegisterMember_ExistsNickname(){
        //given
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        given(memberRepository.findByNickname(anyString()))
                .willReturn(Optional.of(member));

        String password = "password";
        MemberRegisterRequest request = MemberRegisterRequest.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .password(password)
                .build();
        //when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberLoginService.registerNewMember(request)
        );

        //then
        assertEquals(memberException.getErrorCode(), MemberErrorCode.SAME_NICKNAME_EXISTS);
    }

    @Test
    @DisplayName("회원 - Login 성공")
    void testSuccessLoginMember(){
        //given
        member = Member.builder()
                .email("email@naver.com")
                .nickname("nickname")
                .status(MemberStatus.PERMITTED)
                .authType(AuthType.GENERAL)
                .role(Role.ROLE_MEMBER)
                .build();
        member.addPassword("password");

        TokenDto tokens = TokenDto.builder()
                .accessToken("access")
                .refreshToken("refresh")
                .build();

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member));
        given(passwordEncoder.matches(anyString(), anyString()))
                .willReturn(Boolean.TRUE);
        given(tokenService.issueAllToken(any()))
                .willReturn(tokens);
        //when
        TokenDto memberTokens = memberLoginService.login(
                MemberLoginRequest
                        .builder()
                        .email(member.getEmail())
                        .password(member.getPassword())
                        .build()
        );
        //then
        assertEquals(memberTokens.getAccessToken(), tokens.getAccessToken());
        assertEquals(memberTokens.getRefreshToken(), tokens.getRefreshToken());
    }

    @Test
    @DisplayName("로그인 실패 - email 오류")
    void testFailedLoginMember_emailWrong(){
        //given
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());
        //when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberLoginService.login(
                        MemberLoginRequest
                        .builder()
                        .email(member.getEmail())
                        .password(member.getPassword())
                        .build()
                )
        );
        //then
        assertEquals(memberException.getErrorCode(), MemberErrorCode.MEMBER_NOT_FOUND);
    }

    @Test
    @DisplayName("로그인 실패 - password 오류")
    void testFailedLoginMember_passwordWrong(){
        //given
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member));
        member.addPassword("password");

        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(Boolean.FALSE);

        //when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberLoginService.login(
                        MemberLoginRequest
                                .builder()
                                .email(member.getEmail())
                                .password(member.getPassword())
                                .build()
                )
        );
        //then
        assertEquals(memberException.getErrorCode(), MemberErrorCode.PASSWORD_WRONG);
    }

    @Test
    @DisplayName("회원 로그인 실패 - 이메일 인증이 안된 사용자")
    void testFailedLoginMember_emailVerificationFailed(){
        //given
        member.addPassword("password");
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member));
        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(Boolean.TRUE);


        //when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberLoginService.login(
                        MemberLoginRequest
                                .builder()
                                .email(member.getEmail())
                                .password(member.getPassword())
                                .build()
                )
        );

        //then
        assertEquals(memberException.getErrorCode(), MemberErrorCode.EMAIL_VERIFICATION_HAS_NOT_BEEN_COMPLETED);
    }

    @Test
    @DisplayName("회원 로그인 실패 - 정지 당한 사용자")
    void testFailedLoginMember_bannedMember(){
        //given
        Member member = Member.builder()
                .email("email@naver.com")
                .status(MemberStatus.BANNED)
                .build();
        member.addPassword("password");

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member));
        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(Boolean.TRUE);


        //when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberLoginService.login(
                        MemberLoginRequest
                                .builder()
                                .email(member.getEmail())
                                .password(member.getPassword())
                                .build()
                )
        );

        //then
        assertEquals(memberException.getErrorCode(), MemberErrorCode.SUSPEND_USER);
    }

    @Test
    @DisplayName("회원 로그인 실패 - 회원탈퇴한 사용자")
    void testFailedLoginMember_withdrawalMember(){
        //given
        Member member = Member.builder()
                .email("email@naver.com")
                .status(MemberStatus.WITHDRAWAL)
                .build();
        member.addPassword("password");

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member));
        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(Boolean.TRUE);


        //when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberLoginService.login(
                        MemberLoginRequest
                                .builder()
                                .email(member.getEmail())
                                .password(member.getPassword())
                                .build()
                )
        );

        //then
        assertEquals(memberException.getErrorCode(), MemberErrorCode.WITHDRAW_USER);
    }




}