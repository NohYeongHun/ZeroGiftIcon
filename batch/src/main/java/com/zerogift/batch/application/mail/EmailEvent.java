package com.zerogift.batch.application.mail;

import com.zerogift.batch.core.entity.email.EmailMessage;
import com.zerogift.batch.exception.EmailSendException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailEvent {

    private final JavaMailSender javaMailSender;

    private static final String ERROR_MESSAGE = "이메일 보내는중 에러가 발생하였습니다.";

    @Value("${spring.mail.username}")
    private String username;

    public void sendTemplateMail(EmailMessage emailMessage) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        sendTemplate(mimeMessage, emailMessage);
    }

    private void sendTemplate(MimeMessage mimeMessage, EmailMessage emailMessage) {
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(emailMessage.getEmail());
            helper.setText(emailMessage.getMessage(), true);
            helper.setSubject(emailMessage.getStatus().getDescription());
            helper.setFrom(username);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new EmailSendException(ERROR_MESSAGE);
        }
    }

    public void sendMail(EmailMessage emailMessage) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom(username);
        simpleMailMessage.setTo(emailMessage.getEmail());
        simpleMailMessage.setSubject(emailMessage.getStatus().getDescription());
        simpleMailMessage.setText(emailMessage.getMessage());

        javaMailSender.send(simpleMailMessage);
    }

}
