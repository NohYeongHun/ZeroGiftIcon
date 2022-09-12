package com.zerogift.batch.core.service.mail;

import static com.zerogift.batch.data.MakeDataUtils.createMember;
import static com.zerogift.batch.data.MakeDataUtils.createPayHistroy;
import static com.zerogift.batch.data.MakeDataUtils.createProduct;
import static com.zerogift.batch.data.MakeDataUtils.createProductImage;
import static com.zerogift.batch.data.MakeDataUtils.createStaticsEmailMessage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import com.zerogift.batch.application.mail.EmailEvent;
import com.zerogift.batch.core.entity.email.EmailMessage;
import com.zerogift.batch.core.entity.member.Member;
import com.zerogift.batch.core.entity.product.Product;
import com.zerogift.batch.core.entity.product.ProductImage;
import com.zerogift.batch.core.repository.email.EmailMessageRepository;
import com.zerogift.batch.core.repository.member.MemberRepository;
import com.zerogift.batch.core.repository.pay.PayHistoryRepository;
import com.zerogift.batch.core.repository.product.ProductImageRepository;
import com.zerogift.batch.core.repository.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class SendMailServiceTest {

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