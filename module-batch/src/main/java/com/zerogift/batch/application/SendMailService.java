package com.zerogift.batch.application;

import com.zerogift.batch.infrastructure.dto.EmailEvent;
import com.zerogift.email.domain.EmailMessage;
import com.zerogift.email.repository.EmailMessageRepository;
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
