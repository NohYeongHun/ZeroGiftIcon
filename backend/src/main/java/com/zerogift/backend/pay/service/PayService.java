package com.zerogift.backend.pay.service;

import com.zerogift.backend.common.exception.code.MemberErrorCode;
import com.zerogift.backend.common.exception.code.PayErrorCode;
import com.zerogift.backend.common.exception.code.ProductErrorCode;
import com.zerogift.backend.common.exception.member.MemberException;
import com.zerogift.backend.common.exception.pay.NotEnoughPointException;
import com.zerogift.backend.common.exception.pay.NotEnoughProductCountException;
import com.zerogift.backend.common.exception.product.ProductException;
import com.zerogift.backend.giftBox.entity.GiftBox;
import com.zerogift.backend.giftBox.repository.GiftBoxRepository;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.member.repository.MemberRepository;
import com.zerogift.backend.notice.service.NoticeService;
import com.zerogift.backend.notice.type.NoticeType;
import com.zerogift.backend.pay.aop.PayLock;
import com.zerogift.backend.pay.dto.PayHisoryRequest;
import com.zerogift.backend.pay.entity.PayHistory;
import com.zerogift.backend.pay.repository.PayHistoryRepository;
import com.zerogift.backend.product.entity.Product;
import com.zerogift.backend.product.repository.ProductRepository;
import com.zerogift.backend.util.BarcodeUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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

    private final NoticeService noticeService;

    private final BarcodeUtils barcodeUtils;

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
            .code(UUID.randomUUID().toString())
            .sendMember(sendMember)
            .recipientMember(toMember)
            .product(product)
            .payHistory(payHistory)
            .build());
        giftBox.addBarcodeUrl(barcodeUtils.barcodeSave(giftBox.getId(), giftBox.getCode()));

        //sse 전송
        noticeService.sendEvent(giftBox.getSendMember(), giftBox.getRecipientMember(),
                NoticeType.gift, giftBox.getId());
    }

}
