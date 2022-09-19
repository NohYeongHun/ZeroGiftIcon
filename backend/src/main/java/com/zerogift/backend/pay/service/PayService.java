package com.zerogift.backend.pay.service;

import com.zerogift.backend.common.exception.code.MemberErrorCode;
import com.zerogift.backend.common.exception.code.PayErrorCode;
import com.zerogift.backend.common.exception.code.ProductErrorCode;
import com.zerogift.backend.common.exception.member.MemberException;
import com.zerogift.backend.common.exception.pay.NotEnoughPointException;
import com.zerogift.backend.common.exception.product.ProductException;
import com.zerogift.backend.giftBox.entity.GiftBox;
import com.zerogift.backend.giftBox.repository.GiftBoxRepository;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.member.repository.MemberRepository;
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

    private final BarcodeUtils barcodeUtils;

    public void pay(PayHisoryRequest payHisoryRequest, String email) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        checkPoint(member, payHisoryRequest.getUsePoint());

        Member sendMember = memberRepository.findById(payHisoryRequest.getSendId())
            .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        Product product = productRepository.findById(payHisoryRequest.getProductId())
            .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));

        PayHistory payHistory = savePayHistoryAndUsePoint(payHisoryRequest, product, sendMember,
            member);

        saveGiftBox(sendMember, member, payHistory, product);
    }

    private PayHistory savePayHistoryAndUsePoint(PayHisoryRequest payHisoryRequest, Product product,
        Member sendMember, Member member) {
        member.usePoint(payHisoryRequest.getUsePoint());

        return payHistoryRepository.save(PayHistory.builder()
            .impUid(payHisoryRequest.getImpUid())
            .merchantUid(payHisoryRequest.getMerchantUid())
            .name(product.getName())
            .price(product.getPrice() - payHisoryRequest.getUsePoint())
            .pgProvider(payHisoryRequest.getPgProvider())
            .pgTid(payHisoryRequest.getPgTid())
            .usePoint(payHisoryRequest.getUsePoint())
            .product(product)
            .fromMember(member)
            .toMember(sendMember)
            .build());
    }

    private void saveGiftBox(Member sendMember, Member member, PayHistory payHistory,
        Product product) {
        GiftBox giftBox = giftBoxRepository.save(GiftBox.builder()
            .code(UUID.randomUUID().toString())
            .sendMember(sendMember)
            .recipientMember(member)
            .product(product)
            .payHistory(payHistory)
            .build());
        giftBox.addBarcodeUrl(barcodeUtils.barcodeSave(giftBox.getId(), giftBox.getCode()));
    }

    private void checkPoint(Member member, Integer point) {
        if (member.getPoint() - point < 0) {
            throw new NotEnoughPointException(PayErrorCode.NOT_ENOUGH_POINT);
        }
    }

}
