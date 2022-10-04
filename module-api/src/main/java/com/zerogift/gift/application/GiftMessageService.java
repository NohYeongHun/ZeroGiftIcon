package com.zerogift.gift.application;

import com.zerogift.gift.application.dto.GiftMessageDto;
import com.zerogift.gift.application.dto.GiftMessageForm;
import com.zerogift.gift.application.dto.GiftMessageListResponse;
import com.zerogift.gift.application.dto.GiftMessageRequest;
import com.zerogift.gift.domain.GiftBox;
import com.zerogift.gift.repository.GiftBoxRepository;
import com.zerogift.gift.domain.GiftMessage;
import com.zerogift.gift.repository.GiftMessageRepository;
import com.zerogift.global.error.code.MemberErrorCode;
import com.zerogift.global.error.exception.MemberException;
import com.zerogift.member.domain.Member;
import com.zerogift.member.repository.MemberRepository;
import com.zerogift.product.repository.ProductRepository;
import com.zerogift.support.auth.userdetails.LoginInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public Object getGiftMessageList(LoginInfo loginInfo) {
        Member member = memberRepository.findByEmail(loginInfo.getEmail())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        List<GiftMessageListResponse> giftMessageListResponses = giftMessageRepository.findByMember(member)
                .stream().map(x -> GiftMessageListResponse.from(x)).collect(Collectors.toList());
        return giftMessageListResponses;

    }
}
