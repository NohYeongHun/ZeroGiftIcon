package com.zerogift.pay.application;

import com.zerogift.gift.domain.GiftBox;
import com.zerogift.gift.repository.GiftBoxRepository;
import com.zerogift.global.error.code.MemberErrorCode;
import com.zerogift.global.error.code.PayErrorCode;
import com.zerogift.global.error.code.ProductErrorCode;
import com.zerogift.global.error.exception.MemberException;
import com.zerogift.global.error.exception.NotEnoughPointException;
import com.zerogift.global.error.exception.NotEnoughProductCountException;
import com.zerogift.global.error.exception.ProductException;
import com.zerogift.member.domain.Member;
import com.zerogift.member.repository.MemberRepository;
import com.zerogift.notice.application.NoticeService;
import com.zerogift.notice.application.dto.EventInfo;
import com.zerogift.notice.domain.NoticeType;
import com.zerogift.pay.application.dto.PayHisoryRequest;
import com.zerogift.pay.domain.PayHistory;
import com.zerogift.pay.repository.PayHistoryRepository;
import com.zerogift.product.domain.Product;
import com.zerogift.product.repository.ProductRepository;
import com.zerogift.support.utils.BarcodeUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PayService {

    private final MemberRepository memberRepository;
    private final PayHistoryRepository payHistoryRepository;
    private final ProductRepository productRepository;
    private final GiftBoxRepository giftBoxRepository;

    private final BarcodeUtils barcodeUtils;

    private final ApplicationEventPublisher applicationEventPublisher;

    @PayLock
    public void pay(PayHisoryRequest payHisoryRequest, String email) {
        Member sendMember = memberRepository.findByEmail(email)
            .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        enoughUsePoint(sendMember, payHisoryRequest.getUsePoint());

        Member toMember = memberRepository.findById(payHisoryRequest.getSendId())
            .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        Product product = productRepository.findById(payHisoryRequest.getProductId())
            .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));
        enoughProductCount(product);

        PayHistory payHistory = savePayHistoryAndUsePoint(payHisoryRequest, product, toMember,
            sendMember);

        saveGiftBox(sendMember, toMember, payHistory, product);
    }

    private void enoughUsePoint(Member member, int point) {
        if(member.getPoint() < point) {
            throw new NotEnoughPointException(PayErrorCode.NOT_ENOUGH_POINT);
        }
        member.usePoint(point);
    }

    private void enoughProductCount(Product product) {
        if(product.getCount() <= 0) {
            throw new NotEnoughProductCountException(PayErrorCode.NOT_ENOUGH_PRODUCT_COUNT);
        }
        product.payProduct();
    }

    private PayHistory savePayHistoryAndUsePoint(PayHisoryRequest payHisoryRequest, Product product,
        Member toMember, Member sendMember) {

        return payHistoryRepository.save(PayHistory.builder()
            .impUid(payHisoryRequest.getImpUid())
            .merchantUid(payHisoryRequest.getMerchantUid())
            .name(product.getName())
            .price(product.getPrice() - payHisoryRequest.getUsePoint())
            .pgProvider(payHisoryRequest.getPgProvider())
            .pgTid(payHisoryRequest.getPgTid())
            .usePoint(payHisoryRequest.getUsePoint())
            .product(product)
            .fromMember(sendMember)
            .toMember(toMember)
            .build());
    }

    private void saveGiftBox(Member sendMember, Member toMember, PayHistory payHistory,
        Product product) {
        GiftBox giftBox = giftBoxRepository.save(GiftBox.builder()
            .code(UUID.randomUUID().toString().substring(0, 6))
            .sendMember(sendMember)
            .recipientMember(toMember)
            .product(product)
            .payHistory(payHistory)
            .build());
        giftBox.addBarcodeUrl(barcodeUtils.barcodeSave(giftBox.getId(), giftBox.getCode()));

        applicationEventPublisher.publishEvent(
            new EventInfo(giftBox.getSendMember(), giftBox.getRecipientMember(),
                            NoticeType.gift, giftBox.getId()));
    }

}
