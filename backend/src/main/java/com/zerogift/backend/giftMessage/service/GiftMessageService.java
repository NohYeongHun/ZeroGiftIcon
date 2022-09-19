package com.zerogift.backend.giftMessage.service;

import com.zerogift.backend.giftBox.entity.GiftBox;
import com.zerogift.backend.giftBox.repository.GiftBoxRepository;
import com.zerogift.backend.giftMessage.dto.GiftMessageDto;
import com.zerogift.backend.giftMessage.dto.GiftMessageForm;
import com.zerogift.backend.giftMessage.dto.GiftMessageRequest;
import com.zerogift.backend.giftMessage.entity.GiftMessage;
import com.zerogift.backend.giftMessage.repository.GiftMessageRepository;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.member.repository.MemberRepository;
import com.zerogift.backend.product.entity.Product;
import com.zerogift.backend.product.repository.ProductRepository;
import com.zerogift.backend.security.dto.LoginInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GiftMessageService {
    private final GiftMessageRepository giftMessageRepository;
    private final GiftBoxRepository giftBoxRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public GiftMessageForm getGiftMessageForm(Long giftBoxId, LoginInfo loginInfo) {
        return giftMessageRepository.findByGiftMessageForm(giftBoxId, loginInfo.getId());
    }

    public void sendGiftMessage(GiftMessageRequest giftMessageRequest, LoginInfo loginInfo) {
        Member fromMember = memberRepository.findByEmail(loginInfo.getEmail())
            .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
        GiftBox giftBox = giftBoxRepository.findGiftBoxById(giftMessageRequest.getGiftBoxId())
            .orElseThrow(() -> new RuntimeException("선물함에 없는 상품입니다."));

        giftMessageRepository.save(GiftMessage.builder()
                .message(giftMessageRequest.getMessage())
                .toMember(giftBox.getSendMember())
                .fromMember(fromMember)
                .product(giftBox.getProduct())
                .giftBox(giftBox)
                .build());
        giftBox.answer();
    }

    @Transactional(readOnly = true)
    public GiftMessageDto getGiftMessage(Long giftMessageId, LoginInfo loginInfo) {
        return giftMessageRepository.findByGiftMessage(giftMessageId, loginInfo.getId())
            .orElseThrow(() -> new RuntimeException("감사 메시지가 존재하지 않습니다."));
    }

}
