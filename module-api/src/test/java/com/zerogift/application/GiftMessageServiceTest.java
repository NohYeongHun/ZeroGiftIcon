package com.zerogift.application;

import static org.assertj.core.api.Assertions.assertThat;
import static com.zerogift.utils.DataMakeUtils.결제_히스트리_생성;
import static com.zerogift.utils.DataMakeUtils.상품_생성;
import static com.zerogift.utils.DataMakeUtils.상품_이미지_생성;
import static com.zerogift.utils.DataMakeUtils.선물함_생성;
import static com.zerogift.utils.DataMakeUtils.회원_생성;

import com.zerogift.acceptance.AcceptanceTest;
import com.zerogift.gift.application.GiftBoxService;
import com.zerogift.gift.application.GiftMessageService;
import com.zerogift.gift.application.dto.GiftMessageDto;
import com.zerogift.gift.application.dto.GiftMessageForm;
import com.zerogift.gift.application.dto.GiftMessageRequest;
import com.zerogift.gift.domain.GiftBox;
import com.zerogift.gift.repository.GiftBoxRepository;
import com.zerogift.gift.repository.GiftMessageRepository;
import com.zerogift.member.domain.Member;
import com.zerogift.member.repository.MemberRepository;
import com.zerogift.pay.domain.PayHistory;
import com.zerogift.pay.repository.PayHistoryRepository;
import com.zerogift.product.domain.Product;
import com.zerogift.product.domain.ProductImage;
import com.zerogift.product.repository.ProductImageRepository;
import com.zerogift.product.repository.ProductRepository;
import com.zerogift.support.auth.userdetails.AdminInfo;
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