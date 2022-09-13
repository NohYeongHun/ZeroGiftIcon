package com.zerogift.batch.core.service.statistics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.zerogift.batch.core.dto.SaleStatisticsDto;
import com.zerogift.batch.core.entity.email.EmailMessage;
import com.zerogift.batch.core.entity.member.Member;
import com.zerogift.batch.core.repository.email.EmailMessageRepository;
import com.zerogift.batch.core.repository.pay.PayHistoryRepository;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class SaleStatisticsServiceTest {

    @Autowired
    private SaleStatisticsService saleStatisticsService;

    @MockBean
    private PayHistoryRepository payHistoryRepository;

    @DisplayName("HTML Template과 같이 email 값 반환")
    @Test
    void convertEmailTemplateTest() {
        when(payHistoryRepository.findStatisticBySeller(any())).thenReturn(
            Collections.singletonList(SaleStatisticsDto.builder()
                .thumbnail("https://www.naverr.com")
                .productName("테스트 상품")
                .count(1L)
                .price(2L)
                .build())
        );

        EmailMessage emailMessage = saleStatisticsService.convertEmailTemplate(
            Member.builder().build());

        assertThat(emailMessage).isNotNull();
    }

}