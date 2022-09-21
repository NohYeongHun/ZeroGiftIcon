package com.zerogift.backend.email.service;

import com.zerogift.backend.email.entity.EmailAuth;
import com.zerogift.backend.email.repository.EmailAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailAuthRepository emailAuthRepository;
    private final JavaMailSender javaMailSender;
    private static final String ADDRESS_PREFIX = "http://localhost:8080/member-auth/confirm-email?email=";
    private static final String TOKEN_PREFIX = "&authToken=";

    @Transactional
    public void sendEmail(String email){
        EmailAuth emailAuth = emailAuthRepository.save(
                EmailAuth.builder()
                        .email(email)
                        .authToken(UUID.randomUUID().toString())
                        .expired(false)
                        .build()
        );

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailAuth.getEmail());
        message.setSubject("회원가입 이메일 인증");
        message.setText(ADDRESS_PREFIX + emailAuth.getEmail() + TOKEN_PREFIX + emailAuth.getAuthToken());

        javaMailSender.send(message);
    }
}
