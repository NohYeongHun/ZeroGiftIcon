package com.zerogift.backend.giftBox.service;

import static com.zerogift.backend.utils.DataMakeUtils.결제_히스트리_생성;
import static com.zerogift.backend.utils.DataMakeUtils.상품_생성;
import static com.zerogift.backend.utils.DataMakeUtils.상품_이미지_생성;
import static com.zerogift.backend.utils.DataMakeUtils.선물함_생성;
import static com.zerogift.backend.utils.DataMakeUtils.회원_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

import com.zerogift.backend.acceptance.AcceptanceTest;
import com.zerogift.backend.common.dto.MyPageableDto;
import com.zerogift.backend.common.exception.gift.NotEqualsNotBarcodeException;
import com.zerogift.backend.giftBox.dto.GiftBoxDetail;
import com.zerogift.backend.giftBox.dto.GiftBoxDto;
import com.zerogift.backend.giftBox.entity.GiftBox;
import com.zerogift.backend.giftBox.repository.GiftBoxRepository;
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
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Page;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class GiftBoxServiceTest extends AcceptanceTest {

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

    Member member;
    Product product;
    ProductImage productImage;
    PayHistory payHistory;
    GiftBox giftBox;
    AdminInfo adminInfo;

    @BeforeEach
    void setup() throws IOException {
        super.setUp();
        member = memberRepository.save(회원_생성("test2@naver.com", "테스트"));
        product = productRepository.save(상품_생성("테스트 상품", "테스트 상품 설명", member));
        productImage = productImageRepository.save(상품_이미지_생성("https://test.com"));

        payHistory = payHistoryRepository.save(결제_히스트리_생성(product, member, member));
        giftBox = giftBoxRepository.save(선물함_생성(product, member, member, payHistory));
        adminInfo = AdminInfo.builder()
            .id(member.getId())
            .email(member.getEmail())
            .role(member.getRole().name())
            .build();
    }

    @DisplayName("선물함 리스트 조회")
    @Test
    void findByGiftBoxListTest() {
        List<GiftBoxDto> giftBoxDtoPages = giftBoxService.findByGiftBoxList(adminInfo, new MyPageableDto(0, 10));

        GiftBoxDto extract = new GiftBoxDto(giftBox.getId(), product.getName(), null, product.getDescription(), false,
            member.getId(), member.getNickname(), product.getId(), false, false);

        assertThat(giftBoxDtoPages).containsExactly(extract);
    }


    @DisplayName("선물함 상세 페이지 조회")
    @Test
    void findByGiftBoxDeatilaTest() {
        GiftBoxDetail giftBoxDetail = giftBoxService.getGiftBoxDetail(adminInfo, giftBox.getId());

        GiftBoxDetail extract = new GiftBoxDetail(product.getName(), null, giftBox.getBarcodeUrl(), false);

        assertThat(giftBoxDetail).isEqualTo(extract);
    }

    @DisplayName("기프트콘 사용 테스트")
    @Test
    void useGiftConTest() {
        giftBoxService.useGiftCon(giftBox.getId(), giftBox.getCode());

        GiftBox extract = giftBoxRepository.findById(giftBox.getId()).get();

        assertThat(extract.getIsUse()).isTrue();
    }
    
    @DisplayName("기프트콘 사용시 코드가 다르면 에러를 반환")
    @Test
    void notEqulasGiftCodeExceptionTest() {
        assertThatExceptionOfType(NotEqualsNotBarcodeException.class).isThrownBy(() -> {
            giftBoxService.useGiftCon(giftBox.getId(), "test");
        }).withMessage("코드가 일치 하지 않습니다.");
    }
    
}