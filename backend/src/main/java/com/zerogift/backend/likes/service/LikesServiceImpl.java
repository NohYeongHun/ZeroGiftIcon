package com.zerogift.backend.likes.service;

import com.zerogift.backend.common.dto.Result;
import com.zerogift.backend.common.exception.code.MemberErrorCode;
import com.zerogift.backend.common.exception.code.ProductErrorCode;
import com.zerogift.backend.common.exception.member.MemberException;
import com.zerogift.backend.common.exception.product.ProductException;
import com.zerogift.backend.likes.entity.Likes;
import com.zerogift.backend.likes.model.LikesModel;
import com.zerogift.backend.likes.repository.LikesRepository;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.member.repository.MemberRepository;
import com.zerogift.backend.product.entity.Product;
import com.zerogift.backend.product.repository.ProductRepository;
import com.zerogift.backend.security.dto.LoginInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LikesServiceImpl implements LikesService {


    private final LikesRepository likesRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public ResponseEntity<Result<?>> pressLike(LoginInfo loginInfo, Long productId) {
        // 회원 정보 가져오기
        Member member = memberRepository.findByEmail(loginInfo.getEmail())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 상품 정보 가져오기
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));

        // 자기자신의 물건은 좋아요 할수 없음
        if (member.getId() == product.getMember().getId()) {
            throw new ProductException(ProductErrorCode.SELF_VOTE_FORBIDDEN);
        }

        // 이미 좋아요를 눌렀을 경우 다시 좋아요 누를 수 없음
        if (likesRepository.existsByMemberAndProduct(member, product)) {
            throw new ProductException(ProductErrorCode.DUPLICATE_VOTING_FORBIDDEN);
        }

        // 'Likes Entity' 에 좋아요 기록 저장
        Likes likes = Likes.builder()
                .product(product)
                .member(member)
                .regDate(LocalDateTime.now())
                .build();

        // 'Product Entity' 에 해당 상품의 총 좋아요 수 저장
        long likesCount = likesRepository.countByProduct(product);
        product.setLikeCount(likesCount);

        // Response 할 정보 편집
        LikesModel likesModel = LikesModel.of(likes);
        return ResponseEntity.ok().body(Result.builder().data(likesModel).build());
    }

    @Override
    @Transactional
    public ResponseEntity<Result<?>> likeCancel(LoginInfo loginInfo, Long productId) {
        // 회원 정보 가져오기
        Member member = memberRepository.findByEmail(loginInfo.getEmail())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 상품 정보 가져오기
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));

        // 좋아요를 누른적이 없을 경우
        Likes likes = likesRepository.findByMemberAndProduct(member, product)
                .orElseThrow(() -> new ProductException(ProductErrorCode.NEVER_PRESS_LIKE));


        // 'Likes Entity' 에서 좋아요 기록 삭제
        likesRepository.delete(likes);

        // 'Product Entity' 에 해당 상품의 총 좋아요 수 저장
        long likesCount = likesRepository.countByProduct(product);
        product.setLikeCount(likesCount);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Result<?>> likeList(LoginInfo loginInfo) {
        // 회원 정보 가져오기
        Member member = memberRepository.findByEmail(loginInfo.getEmail())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        // List 형식으로 회원이 좋아요한 상품들 가져오기
        List<LikesModel> likesList = likesRepository.findByMember(member);
        return ResponseEntity.ok().body(Result.builder().data(likesList).build());
    }

}
