package com.zerogift.backend.member.service;

import com.zerogift.backend.common.type.AuthType;
import com.zerogift.backend.config.SecurityConfig;
import com.zerogift.backend.email.entity.EmailAuth;
import com.zerogift.backend.email.repository.EmailAuthRepository;
import com.zerogift.backend.email.service.EmailService;
import com.zerogift.backend.member.dto.MemberLoginRequest;
import com.zerogift.backend.member.dto.MemberRegisterRequest;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.member.repository.MemberRepository;
import com.zerogift.backend.member.type.MemberStatus;
import com.zerogift.backend.security.service.TokenService;
import com.zerogift.backend.security.type.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.doNothing;

@EnableAutoConfiguration
@ExtendWith(MockitoExtension.class)
class MemberLoginServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    TokenService tokenService;

    @Mock
    EmailService emailService;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    MemberLoginService memberLoginService;

    private Member member;


    @BeforeEach
    void setup() {
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
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
    void testSuccessRegisterMember(){
        //given
        String password = "password";
        String encodingPassword = passwordEncoder.encode(password);
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

//        given(passwordEncoder.encode(anyString()))
//                .willReturn(encodingPassword);

        doNothing().when(emailService).sendEmail(anyString());

        //when
        Member registerMember = memberLoginService.registerNewMember(request);

        //then
        assertTrue(passwordEncoder.matches(
                password,
                registerMember.getPassword()
        ));
        assertEquals(member.getEmail(), registerMember.getEmail());
        assertEquals(member.getNickname(), registerMember.getNickname());
    }

}