package com.zerogift.backend.product.controller;

import java.util.List;

import javax.validation.Valid;

import com.zerogift.backend.view.service.ViewHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.zerogift.backend.common.dto.Result;
import com.zerogift.backend.config.authorization.AuthenticationPrincipal;
import com.zerogift.backend.product.dto.NewProductRequest;
import com.zerogift.backend.product.service.ProductService;
import com.zerogift.backend.product.type.Category;
import com.zerogift.backend.security.dto.LoginInfo;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ViewHistoryService viewHistoryService;
    
    @PostMapping("admin/product")
    public ResponseEntity<Result<?>> addProduct(
            @RequestBody @Valid NewProductRequest request,
            @AuthenticationPrincipal LoginInfo loginInfo) {
        return productService.addProduct(request, loginInfo);
    }

    @DeleteMapping("admin/product")
    public ResponseEntity<Result<?>> removeProduct(
            @RequestParam Long productId,
            @AuthenticationPrincipal LoginInfo loginInfo) {
        return productService.removeProduct(productId, loginInfo);
    }

    @GetMapping("admin/myproducts")
    public ResponseEntity<Result<?>> listMyProduct(
        @RequestParam Integer idx,
        @RequestParam Integer size,
        @AuthenticationPrincipal LoginInfo loginInfo) {
        return productService.listMyProduct(idx, size, loginInfo);
    }

    @GetMapping("product/search")
    public ResponseEntity<Result<?>> searchProduct(
            @RequestParam String q,
            @RequestParam Integer idx,
            @RequestParam Integer size) {
        return productService.searchProduct(q, idx, size);
    }

    @GetMapping("product/list")
    public ResponseEntity<Result<?>> listProduct(
            @RequestParam List<Category> categories,
            @RequestParam Integer idx,
            @RequestParam Integer size) {
        return productService.listProduct(categories, idx, size);
    }

    @GetMapping("product/detail/{productId}")
    public ResponseEntity<Result<?>> getDetail(@PathVariable Long productId,
                                               @AuthenticationPrincipal LoginInfo loginInfo) {
        return productService.getDetail(productId);
    }
}
