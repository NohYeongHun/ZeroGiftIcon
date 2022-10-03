package com.zerogift.email.infrastructure;

import com.zerogift.email.domain.EmailAuth;
import com.zerogift.email.repository.EmailAuthRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailAuthRepository emailAuthRepository;
    private final JavaMailSender javaMailSender;

    @Value("${server.url}")
    private String ADDRESS;
    private static final String URI_PREFIX = "member-auth/confirm-email?email=";
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
        message.setText(ADDRESS + URI_PREFIX + emailAuth.getEmail() + TOKEN_PREFIX + emailAuth.getAuthToken());

        javaMailSender.send(message);
    }

}
