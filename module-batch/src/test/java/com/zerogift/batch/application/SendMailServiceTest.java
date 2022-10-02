package com.zerogift.batch.application;

import static com.zerogift.batch.utils.MakeDataUtils.createMember;
import static com.zerogift.batch.utils.MakeDataUtils.createPayHistroy;
import static com.zerogift.batch.utils.MakeDataUtils.createProduct;
import static com.zerogift.batch.utils.MakeDataUtils.createProductImage;
import static com.zerogift.batch.utils.MakeDataUtils.createStaticsEmailMessage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import com.zerogift.BatchTestConfig;
import com.zerogift.batch.infrastructure.dto.EmailEvent;
import com.zerogift.email.domain.EmailMessage;
import com.zerogift.email.repository.EmailMessageRepository;
import com.zerogift.member.domain.Member;
import com.zerogift.member.repository.MemberRepository;
import com.zerogift.pay.repository.PayHistoryRepository;
import com.zerogift.product.domain.Product;
import com.zerogift.product.domain.ProductImage;
import com.zerogift.product.repository.ProductImageRepository;
import com.zerogift.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
class SendMailServiceTest extends BatchTestConfig {

    @Autowired
    private SendMailService sendMailService;

    @MockBean
    private EmailEvent emailEvent;

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

    private Member member;
    private Product product;
    private ProductImage productImage;
    private EmailMessage emailMessage;

    @BeforeEach
    void setup() {
        member = createMember();
        memberRepository.save(member);

        product = createProduct(member);
        productRepository.save(product);

        productImage = createProductImage(product);
        productImageRepository.save(productImage);

        payHistoryRepository.save(createPayHistroy(product, member));

        emailMessage = createStaticsEmailMessage(product);
        emailMessageRepository.save(emailMessage);
    }


    @DisplayName("html형식 이메일 보냄")
    @Test
    void sendMailTemplateTest() {
        doNothing().when(emailEvent).sendTemplateMail(any());

        sendMailService.sendMail(emailMessage);

        EmailMessage extract = emailMessageRepository.findById(emailMessage.getId())
            .orElseThrow(RuntimeException::new);

        assertThat(extract.isSend()).isTrue();
    }

    @DisplayName("기본 이메일 보냄")
    @Test
    void sendMailTest() {
        doNothing().when(emailEvent).sendMail(any());

        sendMailService.sendMail(emailMessage);

        EmailMessage extract = emailMessageRepository.findById(emailMessage.getId())
            .orElseThrow(RuntimeException::new);

        assertThat(extract.isSend()).isTrue();
    }

}