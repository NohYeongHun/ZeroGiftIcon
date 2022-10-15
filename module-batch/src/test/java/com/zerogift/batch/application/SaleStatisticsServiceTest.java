package com.zerogift.batch.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.zerogift.BatchTestConfig;
import com.zerogift.email.domain.EmailMessage;
import com.zerogift.member.domain.Member;
import com.zerogift.pay.application.dto.SaleStatisticsDto;
import com.zerogift.pay.repository.PayHistoryRepository;
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
class SaleStatisticsServiceTest extends BatchTestConfig {

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
