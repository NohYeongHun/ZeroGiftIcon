package com.zerogift.backend.likes.service;

import com.zerogift.backend.common.dto.Result;
import com.zerogift.backend.common.exception.MemberException;
import com.zerogift.backend.common.exception.code.MemberErrorCode;
import com.zerogift.backend.common.exception.code.ProductErrorCode;
import com.zerogift.backend.likes.entity.Likes;
import com.zerogift.backend.likes.model.LikesModel;
import com.zerogift.backend.likes.repository.LikesRepository;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.member.repository.MemberRepository;
import com.zerogift.backend.product.dto.ProductDto;
import com.zerogift.backend.product.entity.Product;
import com.zerogift.backend.product.repository.ProductRepository;
import com.zerogift.backend.view.entity.ViewHistory;
import com.zerogift.backend.view.model.ViewModel;
import com.zerogift.backend.view.repository.ViewHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LikesServiceImpl implements LikesService{


    private final LikesRepository likesRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    @Override
    public ResponseEntity<Result<?>> pressLike(String email, Long productId) {
        // 회원 정보 가져오기
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Result.builder().status(403).success(false).data(MemberErrorCode.MEMBER_NOT_FOUND).build()
            );
        }
        Member member = optionalMember.get();

//        // 회원 멤버만 좋아요 누를수 있음
//        if (member.getRole().equals("ROLE_MEMBER")) {
//            return ResponseEntity.badRequest().body(
//                    Result.builder().status(403).success(false).data(ProductErrorCode.VOTE_NOT_ALLOWED_FOR_NON_MEMBER).build()
//            );
//        }

        // 상품 정보 가져오기
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Result.builder().status(404).success(false).data(ProductErrorCode.PRODUCT_NOT_FOUND).build()
            );
        }
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
        productRepository.save(product);

        return ResponseEntity.ok().body(Result.builder().data(LikesModel.of(likes)).build());
    }

    @Override
    public ResponseEntity<Result<?>> likeCancel(String email, Long productId) {
        // 회원 정보 가져오기
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Result.builder().status(403).success(false).data(MemberErrorCode.MEMBER_NOT_FOUND).build()
            );
        }
        Member member = optionalMember.get();

        // 상품 정보 가져오기
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Result.builder().status(404).success(false).data(ProductErrorCode.PRODUCT_NOT_FOUND).build()
            );
        }
        Product product = optionalProduct.get();

        // 좋아요를 누른적이 없을 경우
        Optional<Likes> optionalLikes = likesRepository.findByMemberAndProduct(member, product);
        if (!optionalLikes.isPresent()) {
            return ResponseEntity.badRequest().body(
                    Result.builder().status(404).success(false).data(ProductErrorCode.NEVER_PRESS_LIKE).build()
            );
        }
        Likes likes = optionalLikes.get();

        likesRepository.delete(likes);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Result<?>> likeList(String email) {
        // 회원 정보 가져오기
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Result.builder().status(403).success(false).data(MemberErrorCode.MEMBER_NOT_FOUND).build()
            );
        }
        Member member = optionalMember.get();

        List<Likes> likesList = likesRepository.findByMember(member);
        return ResponseEntity.ok().body(Result.builder().data(likesList).build());
    }

}
