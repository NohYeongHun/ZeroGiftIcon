package com.zerogift.backend.product.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.zerogift.backend.likes.repository.LikesRepository;
import com.zerogift.backend.view.service.ViewHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.zerogift.backend.common.dto.Result;
import com.zerogift.backend.common.exception.code.MemberErrorCode;
import com.zerogift.backend.common.exception.code.ProductErrorCode;
import com.zerogift.backend.common.exception.member.MemberException;
import com.zerogift.backend.common.exception.product.ProductException;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.member.repository.MemberRepository;
import com.zerogift.backend.product.dto.NewProductRequest;
import com.zerogift.backend.product.dto.NewProductResponse;
import com.zerogift.backend.product.dto.ProductDetailDto;
import com.zerogift.backend.product.dto.ProductDto;
import com.zerogift.backend.product.dto.ProductImageDto;
import com.zerogift.backend.product.entity.Product;
import com.zerogift.backend.product.entity.ProductImage;
import com.zerogift.backend.product.repository.ProductImageRepository;
import com.zerogift.backend.product.repository.ProductRepository;
import com.zerogift.backend.product.type.Category;
import com.zerogift.backend.product.type.Status;
import com.zerogift.backend.security.dto.LoginInfo;
import com.zerogift.backend.security.type.Role;
import com.zerogift.backend.util.TokenUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final MemberRepository memberRepository;
    private final LikesRepository likesRepository;

    @Transactional
    public ResponseEntity<Result<?>> addProduct(NewProductRequest request, LoginInfo loginInfo) {
        Member member = memberRepository.findByEmail(loginInfo.getEmail())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        if (!member.getRole().equals(Role.ROLE_ADMIN))
            throw new ProductException(ProductErrorCode.INSUFFICIENT_AUTHORITY);
        Product product = Product
                .builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .status(Status.PUBLIC)
                .member(member)
                .build();
        productRepository.save(product);
        boolean first = true;
        for (Long id : request.getProductImageIds()) {
            ProductImage image = productImageRepository.findById(id).get();
            image.setProduct(product);
            if (first) {
                product.setMainImageUrl(image.getUrl());
                image.setIsMainImage(true);
                first = false;
            }
            productImageRepository.save(image);
        }
        return ok(NewProductResponse.builder().productId(product.getId()).build());
    }

    @Transactional
    public ResponseEntity<Result<?>> removeProduct(Long productId, LoginInfo loginInfo) {
        Member member = memberRepository.findByEmail(loginInfo.getEmail())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        if (!member.getRole().equals(Role.ROLE_ADMIN))
            throw new ProductException(ProductErrorCode.INSUFFICIENT_AUTHORITY);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));
        if (!product.getMember().equals(member))
            throw new ProductException(ProductErrorCode.OWNED_BY_SOMEONE_ELSE);
        for (ProductImage image : productImageRepository.findAllByProduct(product)) {
            productImageRepository.delete(image);
        }
        productRepository.delete(product);
        return ResponseEntity.ok().body(Result.builder().data("successfully deleted").build());
    }

    public ResponseEntity<Result<?>> listMyProduct(Integer idx, Integer size, LoginInfo loginInfo) {
        Member member = memberRepository.findByEmail(loginInfo.getEmail())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        if (!member.getRole().equals(Role.ROLE_ADMIN))
            throw new ProductException(ProductErrorCode.INSUFFICIENT_AUTHORITY);
        Pageable pageable = PageRequest.of(idx, size, Sort.by("updatedAt").descending());
        Page<Product> page = productRepository.findByMember(member, pageable);
        return ResponseEntity.ok().body(Result.builder().data(
                page.getContent().stream()
                        .map(product -> ProductDto.builder()
                                .id(product.getId())
                                .name(product.getName())
                                .description(product.getDescription())
                                .price(product.getPrice())
                                .category(product.getCategory())
                                .viewCount(product.getViewCount())
                                .likeCount(product.getLikeCount().intValue())
                                .mainImageUrl(product.getMainImageUrl())
                                .build())
        ).build());
    }

    public ResponseEntity<Result<?>> searchProduct(String q, Integer idx, Integer size) {
        String email = TokenUtil.getAdminOrMemberEmail();
        Member member = memberRepository.findByEmail(email).get();
        Long memberId = email != null ? member.getId() : null;
        Pageable pageable = PageRequest.of(idx, size, Sort.by("updatedAt").descending());
        Page<Product> page = productRepository.findByStatusAndNameContainingOrDescriptionContaining(Status.PUBLIC, q, q, pageable);
        return ResponseEntity.ok().body(Result.builder().data(
                page.getContent().stream()
                        .map(product -> ProductDto.builder()
                                .id(product.getId())
                                .name(product.getName())
                                .description(product.getDescription())
                                .price(product.getPrice())
                                .category(product.getCategory())
                                .viewCount(product.getViewCount())
                                .likeCount(product.getLikeCount().intValue())
                                .liked(memberId == likesRepository.findByMemberAndProduct(member, product).get().getMember().getId() ? true : false)
                                .mainImageUrl(product.getMainImageUrl())
                                .build())
                        .collect(Collectors.toList())
        ).build());
    }

    @Transactional
    public ResponseEntity<Result<?>> likeProduct(Long productId, LoginInfo loginInfo) {
        Member member = memberRepository.findByEmail(loginInfo.getEmail())
                .orElseThrow(() -> new ProductException(ProductErrorCode.VOTE_NOT_ALLOWED_FOR_NON_MEMBER));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));
        if (product.getMember().equals(member))
            throw new ProductException(ProductErrorCode.SELF_VOTE_FORBIDDEN);
        if (product.getLiked().contains(member.getId()))
            throw new ProductException(ProductErrorCode.DUPLICATE_VOTING_FORBIDDEN);
        product.getLiked().add(member.getId());
        productRepository.save(product);
        return ResponseEntity.ok().body(Result.builder().data("successfully liked").build());
    }

    public ResponseEntity<Result<?>> listProduct(List<Category> categories, Integer idx, Integer size) {
        String email = TokenUtil.getAdminOrMemberEmail();
        Member member = memberRepository.findByEmail(email).get();
        Long memberId = email != null ? member.getId() : null;
        Pageable pageable = PageRequest.of(idx, size, Sort.by("updatedAt").descending());
        Page<Product> page = productRepository.findByStatusAndCategoryIn(Status.PUBLIC, categories, pageable);
        return ResponseEntity.ok().body(Result.builder().data(
                page.getContent().stream()
                        .map(product -> ProductDto.builder()
                                .id(product.getId())
                                .name(product.getName())
                                .description(product.getDescription())
                                .price(product.getPrice())
                                .category(product.getCategory())
                                .viewCount(product.getViewCount())
                                .likeCount(product.getLikeCount().intValue())
                                .liked(memberId == likesRepository.findByMemberAndProduct(member, product).get().getMember().getId() ? true : false)
                                .mainImageUrl(product.getMainImageUrl())
                                .build())
                        .collect(Collectors.toList())
        ).build());
    }


    @Transactional
    public ResponseEntity<Result<?>> getDetail(Long productId) {
        Optional<Product> optProduct = productRepository.findById(productId);
        if (optProduct.isEmpty()) return badRequest(404, ProductErrorCode.PRODUCT_NOT_FOUND);
        Product product = optProduct.get();
        List<ProductImage> images = productImageRepository.findAllByProduct(product);
        return ResponseEntity.ok().body(Result.builder().data(
                ProductDetailDto.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .category(product.getCategory())
                        .viewCount(product.getViewCount())
                        .images(images.stream()
                                .map(i -> ProductImageDto.builder()
                                        .productImageId(i.getId())
                                        .url(i.getUrl())
                                        .isMainImage(i.getIsMainImage())
                                        .build())
                                .collect(Collectors.toList()))
                        .build()
        ).build());
    }

    private ResponseEntity<Result<?>> badRequest(int status, Object errorCode) {
        return ResponseEntity.badRequest().body(
                Result.builder().status(status).success(false).data(errorCode).build());
    }

    private ResponseEntity<Result<?>> ok(Object data) {
        return ResponseEntity.ok().body(Result.builder().data(data).build());
    }
}