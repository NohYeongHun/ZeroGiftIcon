package com.zerogift.backend.product.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zerogift.backend.common.dto.Result;
import com.zerogift.backend.common.exception.code.ProductErrorCode;
import com.zerogift.backend.product.dto.NewProductRequest;
import com.zerogift.backend.product.service.ProductService;
import com.zerogift.backend.product.type.Category;
import com.zerogift.backend.util.TokenUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    
    @PostMapping("admin/product")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Result<?>> addProduct(@RequestBody @Valid NewProductRequest request) {
        String email = TokenUtil.getAdminEmail();
        if (email == null) return badRequest(403, ProductErrorCode.INSUFFICIENT_AUTHORITY);
        return ResponseEntity.ok().body(
                Result.builder().data(productService.addProduct(request, email)).build());
    }

    @PostMapping("temp/product")
    public ResponseEntity<Result<?>> addProductTemp(@RequestBody @Valid NewProductRequest request) {
        return ResponseEntity.ok().body(
                Result.builder().data(productService.addProduct(request, "temp@temp")).build());
    }

    @DeleteMapping("admin/product")
    public ResponseEntity<Result<?>> removeProduct(@RequestParam Long productId) {
        String email = TokenUtil.getAdminEmail();
        if (email == null) return badRequest(403, ProductErrorCode.INSUFFICIENT_AUTHORITY);
        return productService.removeProduct(productId, email);
    }

    @DeleteMapping("temp/product")
    public ResponseEntity<Result<?>> removeProductTemp(@RequestParam Long productId) {
        return productService.removeProduct(productId, "temp@temp");
    }

    @PatchMapping("member/product/like")
    public ResponseEntity<Result<?>> likeProduct(@RequestParam Long productId) {
        String email = TokenUtil.getAdminEmail();
        if (email == null) email = TokenUtil.getMemberEmail();
        return productService.likeProduct(productId, email);
    }

    @PatchMapping("temp/product/like")
    public ResponseEntity<Result<?>> likeProductTemp(@RequestParam Long productId) {
        return productService.likeProduct(productId, "temp@temp");
    }

    @GetMapping("product/list")
    public ResponseEntity<Result<?>> listProduct(
            @RequestParam List<Category> categories,
            @RequestParam Integer idx,
            @RequestParam Integer size) {
        return ResponseEntity.ok().body(
            Result.builder().data(productService.listProduct(categories, idx, size)).build());
    }

    @GetMapping("product/detail/{productId}")
    public ResponseEntity<Result<?>> getDetail(@PathVariable Long productId) {
        return ResponseEntity.ok().body(
            Result.builder().data(productService.getDetail(productId)).build());
    }

    private ResponseEntity<Result<?>> badRequest(int status, ProductErrorCode errorCode) {
        return ResponseEntity.badRequest().body(
                Result.builder().status(status).success(false).data(errorCode).build());
    }
}
