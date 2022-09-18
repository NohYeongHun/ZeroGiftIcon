package com.zerogift.backend.likes.service;

import com.zerogift.backend.common.dto.Result;
import com.zerogift.backend.common.exception.MemberException;
import com.zerogift.backend.common.exception.ProductException;
import com.zerogift.backend.common.exception.code.MemberErrorCode;
import com.zerogift.backend.common.exception.code.ProductErrorCode;
import com.zerogift.backend.likes.entity.Likes;
import com.zerogift.backend.likes.model.LikesModel;
import com.zerogift.backend.likes.repository.LikesRepository;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.member.repository.MemberRepository;
import com.zerogift.backend.product.entity.Product;
import com.zerogift.backend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LikesServiceImpl implements LikesService {


    private final LikesRepository likesRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public ResponseEntity<Result<?>> pressLike(String email, Long productId) {
        // 회원 정보 가져오기
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        optionalMember.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        Member member = optionalMember.get();

        // 상품 정보 가져오기
        Optional<Product> optionalProduct = productRepository.findById(productId);
        optionalProduct.orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));
        Product product = optionalProduct.get();

        // 자기자신의 물건 조회해도 조회수는 오르지 않음
        if (member.getId() == product.getMember().getId()) {
            return ResponseEntity.badRequest().body(
                    Result.builder().status(404).success(false).data(ProductErrorCode.SELF_VOTE_FORBIDDEN).build()
            );
        }

        // 이미 좋아요를 눌렀을 경우
        if (likesRepository.countByMemberAndProduct(member, product) > 0) {
            return ResponseEntity.badRequest().body(
                    Result.builder().status(404).success(false).data(ProductErrorCode.DUPLICATE_VOTING_FORBIDDEN).build()
            );
        }

        // 좋아요 기록 저장
        Likes likes = Likes.builder()
                .product(product)
                .member(member)
                .regDate(LocalDateTime.now())
                .build();
        likesRepository.save(likes);

        // 상품이 받은 총 좋아요 수 'Product Entity' 에 저장
        long likesCount = likesRepository.countByProduct(product);
        product.setLikeCount(likesCount);

        // Response 할 정보 편집
        LikesModel likesModel = LikesModel.of(likes);
        return ResponseEntity.ok().body(Result.builder().data(likesModel).build());
    }

    @Override
    @Transactional
    public ResponseEntity<Result<?>> likeCancel(String email, Long productId) {
        // 회원 정보 가져오기
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        optionalMember.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        Member member = optionalMember.get();

        // 상품 정보 가져오기
        Optional<Product> optionalProduct = productRepository.findById(productId);
        optionalProduct.orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));
        Product product = optionalProduct.get();

        // 좋아요를 누른적이 없을 경우
        Optional<Likes> optionalLikes = likesRepository.findByMemberAndProduct(member, product);
        optionalLikes.orElseThrow(() -> new ProductException(ProductErrorCode.NEVER_PRESS_LIKE));
        Likes likes = optionalLikes.get();

        // 'Likes Entity' 에서 삭제
        likesRepository.delete(likes);

        // 상품이 받은 총 좋아요 수 'Product Entity' 에 저장
        long likesCount = likesRepository.countByProduct(product);
        product.setLikeCount(likesCount);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Result<?>> likeList(String email) {
        // 회원 정보 가져오기
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        optionalMember.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        Member member = optionalMember.get();

        List<LikesModel> likesList = likesRepository.findByMember(member);
        return ResponseEntity.ok().body(Result.builder().data(likesList).build());
    }

}
