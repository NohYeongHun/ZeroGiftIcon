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
import com.zerogift.backend.product.dto.NewProductRequest;
import com.zerogift.backend.product.service.ProductService;
import com.zerogift.backend.product.type.Category;


import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    
    @PostMapping("admin/product")
    public ResponseEntity<Result<?>> addProduct(@RequestBody @Valid NewProductRequest request) {
        return productService.addProduct(request);
    }

    @DeleteMapping("admin/product")
    public ResponseEntity<Result<?>> removeProduct(@RequestParam Long productId) {
        return productService.removeProduct(productId);
    }

    @PatchMapping("member/product/like")
    public ResponseEntity<Result<?>> likeProduct(@RequestParam Long productId) {
        return productService.likeProduct(productId);
    }

    @GetMapping("product/list")
    public ResponseEntity<Result<?>> listProduct(
            @RequestParam List<Category> categories,
            @RequestParam Integer idx,
            @RequestParam Integer size) {
        return productService.listProduct(categories, idx, size);
    }

    @GetMapping("product/detail/{productId}")
    public ResponseEntity<Result<?>> getDetail(@PathVariable Long productId) {
        return productService.getDetail(productId);
    }
}
