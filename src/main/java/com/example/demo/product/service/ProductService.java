package com.example.demo.product.service;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.common.dto.Result;
import com.example.demo.common.exception.MemberException;
import com.example.demo.common.exception.ProductException;
import com.example.demo.common.exception.code.MemberErrorCode;
import com.example.demo.common.exception.code.ProductErrorCode;
import com.example.demo.member.entity.Member;
import com.example.demo.member.repository.MemberRepository;
import com.example.demo.product.dto.NewProductRequest;
import com.example.demo.product.dto.NewProductResponse;
import com.example.demo.product.dto.ProductDetailDto;
import com.example.demo.product.dto.ProductDto;
import com.example.demo.product.dto.ProductImageDto;
import com.example.demo.product.entity.Product;
import com.example.demo.product.entity.ProductImage;
import com.example.demo.product.repository.ProductImageRepository;
import com.example.demo.product.repository.ProductRepository;
import com.example.demo.product.type.Category;
import com.example.demo.product.type.Status;
import com.example.demo.util.TokenUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final MemberRepository memberRepository;

    private static final String UPLOAD_PATH = "/opt/zerogift/upload/";

    @Transactional
    public NewProductResponse addProduct(NewProductRequest request, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
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
        return NewProductResponse.builder()
                                 .productId(product.getId())
                                 .build();
    }

    @Transactional
    public ResponseEntity<Result<?>> removeProduct(Long productId, String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalMember.isEmpty()) return badRequest(403, MemberErrorCode.MEMBER_NOT_FOUND);
        if (optionalProduct.isEmpty()) return badRequest(404, ProductErrorCode.PRODUCT_NOT_FOUND);
        Member member = optionalMember.get();
        Product product = optionalProduct.get();
        if (!product.getMember().equals(member)) return badRequest(403, ProductErrorCode.OWNED_BY_SOMEONE_ELSE);
        for (ProductImage image : productImageRepository.findAllByProduct(product)) {
            String url = image.getUrl();
            if (productImageRepository.countByUrl(url) == 1) {
                File file = new File(UPLOAD_PATH + url);
                file.delete();
            }
            productImageRepository.delete(image);
        }
        productRepository.delete(product);
        return ResponseEntity.ok().body(Result.builder().data("successfully deleted").build());
    }

    @Transactional
    public ResponseEntity<Result<?>> likeProduct(Long productId, String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalMember.isEmpty()) return badRequest(403, ProductErrorCode.VOTE_NOT_ALLOWED_FOR_NON_MEMBER);
        if (optionalProduct.isEmpty()) return badRequest(404, ProductErrorCode.PRODUCT_NOT_FOUND);
        Member member = optionalMember.get();
        Product product = optionalProduct.get();
        if (product.getMember().equals(member)) return badRequest(403, ProductErrorCode.SELF_VOTE_FORBIDDEN);
        if (product.getLiked().contains(member.getId())) return badRequest(403, ProductErrorCode.DUPLICATE_VOTING_FORBIDDEN);
        product.getLiked().add(member.getId());
        productRepository.save(product);
        return ResponseEntity.ok().body(Result.builder().data("successfully liked").build());
    }

    public List<ProductDto> listProduct(List<Category> categories, Integer idx, Integer size) {
        String email = TokenUtil.getAdminEmail();
        if (email == null) email = TokenUtil.getMemberEmail();
        Long memberId = email != null ? memberRepository.findByEmail(email).get().getId() : null;
        Pageable pageable = PageRequest.of(idx, size, Sort.by("updatedAt").descending());
        Page<Product> page = productRepository.findByStatusAndCategoryIn(Status.PUBLIC, categories, pageable);
        return page.getContent().stream()
                   .map(product -> ProductDto.builder()
                           .id(product.getId())
                           .name(product.getName())
                           .description(product.getDescription())
                           .price(product.getPrice())
                           .category(product.getCategory())
                           .viewCount(product.getViewCount())
                           .likeCount(product.getLiked().size())
                           .liked(memberId == null ? false : product.getLiked().contains(memberId))
                           .mainImageUrl(product.getMainImageUrl())
                           .build())
                   .collect(Collectors.toList());
    }

    @Transactional
    public ProductDetailDto getDetail(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));
        product.setViewCount(product.getViewCount() + 1);
        List<ProductImage> images = productImageRepository.findAllByProduct(product);
        return ProductDetailDto.builder()
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
                               .build();
    }

    private ResponseEntity<Result<?>> badRequest(int status, Object errorCode) {
        return ResponseEntity.badRequest().body(
                Result.builder().status(status).success(false).data(errorCode).build());
    }
}
