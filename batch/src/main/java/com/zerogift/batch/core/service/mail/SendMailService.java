package com.zerogift.batch.core.service.mail;

import com.zerogift.batch.application.mail.EmailEvent;
import com.zerogift.batch.core.entity.email.EmailMessage;
import com.zerogift.batch.core.repository.email.EmailMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SendMailService {
    private final EmailEvent mailService;
    private final EmailMessageRepository emailMessageRepository;

    public void sendMailTemplate(EmailMessage emailMessage) {
        mailService.sendTemplateMail(emailMessage);
        statusUpdate(emailMessage);
    }

    public void sendMail(EmailMessage emailMessage) {
        mailService.sendMail(emailMessage);
        statusUpdate(emailMessage);
    }

    private void statusUpdate(EmailMessage emailMessage) {
        emailMessage.successMailSend();
        emailMessageRepository.save(emailMessage);
    }

}
