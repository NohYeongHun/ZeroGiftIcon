package com.example.demo.product.controller;

import java.util.List;

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
        if (email == null) return badRequest(403, ProductErrorCode.INSUFFICIENT_AUTHORITY);
        return ResponseEntity.ok().body(
                Result.builder().data(productService.addProduct(request, email)).build());
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
