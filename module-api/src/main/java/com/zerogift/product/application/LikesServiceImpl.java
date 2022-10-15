package com.zerogift.product.application;

import com.zerogift.global.error.code.MemberErrorCode;
import com.zerogift.global.error.code.ProductErrorCode;
import com.zerogift.global.error.exception.MemberException;
import com.zerogift.global.error.exception.ProductException;
import com.zerogift.member.domain.Member;
import com.zerogift.member.repository.MemberRepository;
import com.zerogift.product.application.dto.LikesModel;
import com.zerogift.product.domain.Likes;
import com.zerogift.product.domain.Product;
import com.zerogift.product.repository.LikesRepository;
import com.zerogift.product.repository.ProductRepository;
import com.zerogift.support.auth.userdetails.LoginInfo;
import com.zerogift.support.dto.Result;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

        // 좋아요 했는지 체크
        Optional<Likes> optionalLikes = likesRepository.findByMemberAndProduct(member, product);
        if (!optionalLikes.isPresent()) {
            // 'Likes Entity' 에 좋아요 기록 저장
            Likes likes = Likes.builder()
                .product(product)
                .member(member)
                .regDate(LocalDateTime.now())
                .build();
            likesRepository.save(likes);

            saveLikesCount(product);

            return ResponseEntity.ok(Result.builder().data("좋아요를 눌렀습니다.").build());
        } else {
            Likes likes = optionalLikes.get();
            // 좋아요 누른적이 있으면 좋아요 취소
            likesRepository.delete(likes);

            saveLikesCount(product);

            return ResponseEntity.ok(Result.builder().data("좋아요를 취소하였습니다.").build());
        }

    }

    private void saveLikesCount(Product product) {
        // 'Product Entity' 에 해당 상품의 총 좋아요 수 저장
        long likesCount = likesRepository.countByProduct(product);
        product.setLikeCount(likesCount);
    }

    @Override
    public List<LikesModel> likeList(LoginInfo loginInfo) {
        // 회원 정보 가져오기
        Member member = memberRepository.findByEmail(loginInfo.getEmail())
            .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        // List 형식으로 회원이 좋아요한 상품들 가져오기
        List<LikesModel> likesList = likesRepository.findByMember(member);
        return likesList;
    }
}
