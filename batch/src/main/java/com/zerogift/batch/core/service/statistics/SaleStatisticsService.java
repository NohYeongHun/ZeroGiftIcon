package com.zerogift.batch.core.service.statistics;

import com.zerogift.batch.core.dto.SaleStatisticsDto;
import com.zerogift.batch.core.entity.email.EmailMessage;
import com.zerogift.batch.core.entity.member.Member;
import com.zerogift.batch.core.entity.email.MessageStatus;
import com.zerogift.batch.core.repository.email.EmailMessageRepository;
import com.zerogift.batch.core.repository.pay.PayHistoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Transactional
public class SaleStatisticsService {
    private final PayHistoryRepository payHistoryRepository;
    private final TemplateEngine htmlTemplateEngine;
    private final EmailMessageRepository emailMessageRepository;

    private final static String TITLE = "판매자 월별 정산";
    private final static String TEMPLATE_URL = "mail/statistics";

    public EmailMessage convertEmailTemplate(Member seller) {
        List<SaleStatisticsDto> saleStatisticsDtos = payHistoryRepository.findStatisticBySeller(seller);
        long monthSum = saleStatisticsDtos.stream()
            .mapToLong(SaleStatisticsDto::getPrice)
            .sum();

        Context context = new Context();
        context.setVariable("mailList", saleStatisticsDtos);
        context.setVariable("monthSum", monthSum);
        context.setVariable("title", TITLE);
        String messageTemplate = htmlTemplateEngine.process(TEMPLATE_URL, context);

        return EmailMessage.builder()
            .email(seller.getEmail())
            .message(messageTemplate)
            .status(MessageStatus.STATISTIC)
            .build();
    }

    public void saveMessageQueue(EmailMessage emailMessage) {
        emailMessageRepository.save(emailMessage);
    }

}
