package com.zerogift.product.application;

import com.zerogift.global.error.code.MemberErrorCode;
import com.zerogift.global.error.code.ProductErrorCode;
import com.zerogift.global.error.exception.MemberException;
import com.zerogift.global.error.exception.ProductException;
import com.zerogift.member.domain.Member;
import com.zerogift.member.domain.Role;
import com.zerogift.member.repository.MemberRepository;
import com.zerogift.product.application.dto.MyProductDto;
import com.zerogift.product.application.dto.NewProductRequest;
import com.zerogift.product.application.dto.NewProductResponse;
import com.zerogift.product.application.dto.ProductDetailDto;
import com.zerogift.product.application.dto.ProductDto;
import com.zerogift.product.application.dto.ProductImageDto;
import com.zerogift.product.domain.Category;
import com.zerogift.product.domain.Product;
import com.zerogift.product.domain.ProductImage;
import com.zerogift.product.domain.Status;
import com.zerogift.product.repository.LikesRepository;
import com.zerogift.product.repository.ProductImageRepository;
import com.zerogift.product.repository.ProductRepository;
import com.zerogift.support.auth.token.TokenUtil;
import com.zerogift.support.auth.userdetails.LoginInfo;
import com.zerogift.support.dto.Result;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
        if (!member.getRole().equals(Role.ROLE_ADMIN)) {
            throw new ProductException(ProductErrorCode.INSUFFICIENT_AUTHORITY);
        }
        Product product = Product
                .builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .count(request.getCount())
                .category(request.getCategory())
                .status(Status.PUBLIC)
                .member(member)
                .build();
        productRepository.save(product);
        boolean first = true;
        for (Long id : request.getProductImageIds()) {
            ProductImage image = productImageRepository.findById(id).
                    orElseThrow(() -> new ProductException(ProductErrorCode.NON_EXISTENT_IMAGE_ID));
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
        if (!member.getRole().equals(Role.ROLE_ADMIN)) {
            throw new ProductException(ProductErrorCode.INSUFFICIENT_AUTHORITY);
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));
        if (!product.getMember().equals(member)) {
            throw new ProductException(ProductErrorCode.OWNED_BY_SOMEONE_ELSE);
        }
        for (ProductImage image : productImageRepository.findAllByProduct(product)) {
            productImageRepository.delete(image);
        }
        product.setStatus(Status.DELETED);
        return ok("successfully deleted");
    }

    public ResponseEntity<Result<?>> listMyProduct(Integer idx, Integer size, LoginInfo loginInfo) {
        Member member = memberRepository.findByEmail(loginInfo.getEmail())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        if (!member.getRole().equals(Role.ROLE_ADMIN)) {
            throw new ProductException(ProductErrorCode.INSUFFICIENT_AUTHORITY);
        }
        Pageable pageable = PageRequest.of(idx, size, Sort.by("updatedAt").descending());
        Page<Product> page = productRepository.findByMemberAndStatusNot(member, Status.DELETED, pageable);
        return ok(page.getContent().stream().map(
                product -> MyProductDto.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .inventory(product.getCount())
                        .category(product.getCategory())
                        .createdAt(product.getCreatedAt())
                        .mainImageUrl(product.getMainImageUrl())
                        .build())
        );
    }

    public ResponseEntity<Result<?>> searchProduct(String q, Integer idx, Integer size) {
        Member member = memberRepository.findByEmail(TokenUtil.getAdminOrMemberEmail())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        Pageable pageable = PageRequest.of(idx, size, Sort.by("updatedAt").descending());
        Page<Product> page = productRepository.findByStatusAndNameContainsOrStatusAndDescriptionContains(
                Status.PUBLIC, q, Status.PUBLIC, q, pageable);
        return ok(page.getContent().stream().map(
                        product -> ProductDto.builder()
                                .id(product.getId())
                                .name(product.getName())
                                .description(product.getDescription())
                                .price(product.getPrice())
                                .category(product.getCategory())
                                .viewCount(product.getViewCount())
                                .likeCount(product.getLikeCount().intValue())
                                .liked(likesRepository.existsByMemberAndProduct(member, product) ? true : false)
                                .mainImageUrl(product.getMainImageUrl())
                                .build())
                .collect(Collectors.toList())
        );
    }

    public ResponseEntity<Result<?>> listProduct(List<Category> categories, Integer idx,
                                                 Integer size) {
        Member member = memberRepository.findByEmail(TokenUtil.getAdminOrMemberEmail())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        Pageable pageable = PageRequest.of(idx, size, Sort.by("updatedAt").descending());
        Page<Product> page = productRepository.findByStatusAndCategoryIn(Status.PUBLIC, categories,
                pageable);
        return ok(page.getContent().stream().map(
                        product -> ProductDto.builder()
                                .id(product.getId())
                                .name(product.getName())
                                .description(product.getDescription())
                                .price(product.getPrice())
                                .category(product.getCategory())
                                .viewCount(product.getViewCount())
                                .likeCount(product.getLikeCount().intValue())
                                .liked(likesRepository.existsByMemberAndProduct(member, product) ? true : false)
                                .mainImageUrl(product.getMainImageUrl())
                                .build())
                .collect(Collectors.toList())
        );
    }


    @Transactional
    public ResponseEntity<Result<?>> getDetail(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));
        if (product.getStatus().equals(Status.PRIVATE)) {
            Member member = memberRepository.findByEmail(TokenUtil.getAdminOrMemberEmail())
                    .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
            if (!product.getMember().equals(member)) {
                throw new ProductException(ProductErrorCode.PRIVATE_PRODUCT);
            }
        }
        List<ProductImage> images = productImageRepository.findAllByProduct(product);
        return ok(ProductDetailDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .viewCount(product.getViewCount())
                .likeCount(product.getLikeCount())
                .images(images.stream()
                        .map(i -> ProductImageDto.builder()
                                .productImageId(i.getId())
                                .url(i.getUrl())
                                .isMainImage(i.getIsMainImage())
                                .build())
                        .collect(Collectors.toList()))
                .build()
        );
    }

    private ResponseEntity<Result<?>> ok(Object data) {
        return ResponseEntity.ok().body(Result.builder().data(data).build());
    }
}