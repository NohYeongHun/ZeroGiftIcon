package com.zerogift.backend.giftMessage.service;

import static com.zerogift.backend.utils.DataMakeUtils.결제_히스트리_생성;
import static com.zerogift.backend.utils.DataMakeUtils.상품_생성;
import static com.zerogift.backend.utils.DataMakeUtils.상품_이미지_생성;
import static com.zerogift.backend.utils.DataMakeUtils.선물함_생성;
import static com.zerogift.backend.utils.DataMakeUtils.회원_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.zerogift.backend.acceptance.AcceptanceTest;
import com.zerogift.backend.giftBox.entity.GiftBox;
import com.zerogift.backend.giftBox.repository.GiftBoxRepository;
import com.zerogift.backend.giftBox.service.GiftBoxService;
import com.zerogift.backend.giftMessage.dto.GiftMessageDto;
import com.zerogift.backend.giftMessage.dto.GiftMessageForm;
import com.zerogift.backend.giftMessage.dto.GiftMessageRequest;
import com.zerogift.backend.giftMessage.repository.GiftMessageRepository;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.member.repository.MemberRepository;
import com.zerogift.backend.pay.entity.PayHistory;
import com.zerogift.backend.pay.repository.PayHistoryRepository;
import com.zerogift.backend.product.entity.Product;
import com.zerogift.backend.product.entity.ProductImage;
import com.zerogift.backend.product.repository.ProductImageRepository;
import com.zerogift.backend.product.repository.ProductRepository;
import com.zerogift.backend.security.dto.AdminInfo;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class GiftMessageServiceTest extends AcceptanceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private PayHistoryRepository payHistoryRepository;

    @Autowired
    private GiftBoxRepository giftBoxRepository;

    @Autowired
    private GiftBoxService giftBoxService;

    @Autowired
    private GiftMessageRepository giftMessageRepository;

    @Autowired
    private GiftMessageService giftMessageService;

    Member member;
    Product product;
    ProductImage productImage;
    PayHistory payHistory;
    GiftBox giftBox;
    AdminInfo adminInfo;

    @BeforeEach
    void setup() throws IOException {
        super.setUp();
        member = memberRepository.save(회원_생성("test1@naver.com", "테스트"));
        product = productRepository.save(상품_생성("테스트 상품", "테스트 상품 설명", member));
        productImage = productImageRepository.save(상품_이미지_생성("https://test.com"));

        payHistory = payHistoryRepository.save(결제_히스트리_생성(product, member, member));
        giftBox = giftBoxRepository.save(선물함_생성(product, member, member, payHistory));
        adminInfo = createAdminInfo();
    }

    private AdminInfo createAdminInfo() {
        return AdminInfo.builder()
            .id(member.getId())
            .email(member.getEmail())
            .role(member.getRole().name())
            .build();
    }

    @DisplayName("감사 메시지 등록 폼 조회")
    @Test
    void getGiftMessageFormTest() {
        GiftMessageForm giftMessageForm = giftMessageService.getGiftMessageForm(giftBox.getId(), adminInfo);

        assertThat(giftMessageForm).isEqualTo(new GiftMessageForm(giftBox.getSendMember().getNickname(), null));
    }

    @DisplayName("감사 메시지 보내기")
    @Test
    void sendGiftMessageTest() {
        giftMessageService.sendGiftMessage(createRequest(), adminInfo);

        assertThat(giftMessageRepository.findAll()).hasSize(1);
    }

    private GiftMessageRequest createRequest() {
        return GiftMessageRequest.builder()
            .giftBoxId(giftBox.getId())
            .sendMemberId(member.getId())
            .productId(product.getId())
            .message("생일 축하")
            .build();
    }

    @DisplayName("감사 메시지 상세 보기")
    @Test
    void getGiftMessageTest() {
        giftMessageService.sendGiftMessage(createRequest(), adminInfo);

        GiftMessageDto giftMessageDto = giftMessageService.getGiftMessage(giftMessageRepository.findAll().get(0).getId(), adminInfo);

        assertThat(giftMessageDto).isEqualTo(new GiftMessageDto(null, "테스트", "생일 축하"));
    }


}