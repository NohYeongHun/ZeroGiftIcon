package com.zerogift.backend.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.zerogift.backend.common.type.AuthType;
import com.zerogift.backend.email.service.EmailService;
import com.zerogift.backend.member.dto.MemberRegisterRequest;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.member.repository.MemberRepository;
import com.zerogift.backend.member.type.MemberStatus;
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

}