package com.zerogift.batch.job.statistics;

import static com.zerogift.batch.data.MakeDataUtils.createMember;
import static com.zerogift.batch.data.MakeDataUtils.createPayHistroy;
import static com.zerogift.batch.data.MakeDataUtils.createProduct;
import static com.zerogift.batch.data.MakeDataUtils.createProductImage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import com.zerogift.batch.BatchTestConfig;
import com.zerogift.batch.application.client.SlackEvent;
import com.zerogift.batch.core.entity.email.EmailMessage;
import com.zerogift.batch.core.entity.member.Member;
import com.zerogift.batch.core.entity.product.Product;
import com.zerogift.batch.core.entity.product.ProductImage;
import com.zerogift.batch.core.repository.email.EmailMessageRepository;
import com.zerogift.batch.core.repository.member.MemberRepository;
import com.zerogift.batch.core.repository.pay.PayHistoryRepository;
import com.zerogift.batch.core.repository.product.ProductImageRepository;
import com.zerogift.batch.core.repository.product.ProductRepository;
import com.zerogift.batch.core.service.statistics.SaleStatisticsService;
import com.zerogift.batch.job.listener.SlackJobListener;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBatchTest
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {SaleStatisticsConfig.class, BatchTestConfig.class,
    SlackJobListener.class, SaleStatisticsService.class})
class SaleStatisticsConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private PayHistoryRepository payHistoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private EmailMessageRepository emailMessageRepository;

    @MockBean
    private SlackEvent slackEvent;

    private Member member;
    private Product product;
    private ProductImage productImage;

    @BeforeEach
    void setup() {
        member = createMember();
        memberRepository.save(member);

        product = createProduct(member);
        productRepository.save(product);

        productImage = createProductImage(product);
        productImageRepository.save(productImage);

        payHistoryRepository.save(createPayHistroy(product, member));
        payHistoryRepository.save(createPayHistroy(product, member));
    }

    @DisplayName("판매자 통게 내는 Job")
    @Test
    void saleStatisticsConfigTest() throws Exception {
        doNothing().when(slackEvent).send(any());

        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        List<EmailMessage> email = emailMessageRepository.findAll();

        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(email).hasSize(1);
    }

}