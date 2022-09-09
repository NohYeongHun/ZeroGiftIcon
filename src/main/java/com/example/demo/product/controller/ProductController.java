package com.example.demo.product.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.dto.Result;
import com.example.demo.common.exception.code.ProductErrorCode;
import com.example.demo.product.dto.NewProductRequest;
import com.example.demo.product.service.ProductService;
import com.example.demo.product.type.Category;
import com.example.demo.security.dto.AdminInfo;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    
    @PostMapping("admin/product")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Result<?>> addProduct(@RequestBody @Valid NewProductRequest request) {
        String email = getEmail();
        if (email == null) {
            return ResponseEntity.badRequest().body(
                Result.builder()
                        .status(403)
                        .success(false)
                        .data(ProductErrorCode.INSUFFICIENT_AUTHORITY)
                        .build()
            );
        }
        return ResponseEntity.ok().body(
                Result.builder()
                        .status(200)
                        .success(true)
                        .data(productService.addProduct(request, email))
                        .build()
        );
    }

    @GetMapping("product/list")
    public ResponseEntity<Result<?>> listProduct(
            @RequestParam Category category,
            @RequestParam Integer idx,
            @RequestParam Integer size) {
        return ResponseEntity.ok().body(
            Result.builder()
                  .status(200)
                  .success(true)
                  .data(productService.listProduct(category, idx, size))
                  .build()
        );
    }

    @GetMapping("product/detail/{productId}")
    public ResponseEntity<Result<?>> getDetail(@PathVariable Long productId) {
        return ResponseEntity.ok().body(
            Result.builder()
                  .status(200)
                  .success(true)
                  .data(productService.getDetail(productId))
                  .build()
        );
    }

    private String getEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = null;
        try {
            email = ((AdminInfo) auth.getPrincipal()).getEmail();
        } catch (Exception e) {
            return email;
        }
        return email;
    }
}
