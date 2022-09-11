package com.example.demo.product.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.common.dto.Result;
import com.example.demo.common.exception.code.ProductErrorCode;
import com.example.demo.product.service.ProductImageService;
import com.example.demo.security.dto.AdminInfo;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductImageController {
    private final ProductImageService productImageService;

    @PostMapping("admin/upload")
    public ResponseEntity<Result<?>> uploadImages(
            @RequestParam("files") MultipartFile[] request) {
        String email = getEmail();
        if (email == null) return getResponse(403, ProductErrorCode.INSUFFICIENT_AUTHORITY);
        return ResponseEntity.ok().body(
            Result.builder().data(productImageService.upload(request)).build());
    }

    @PostMapping("temp/upload")
    public ResponseEntity<Result<?>> uploadImagesTemp(
            @RequestParam("files") MultipartFile[] request) {
        return ResponseEntity.ok().body(
            Result.builder().data(productImageService.upload(request)).build());
    }

    @GetMapping("img/{url}")
    public ResponseEntity<byte[]> displayImage(
            @PathVariable String url) {
        return productImageService.display(url);
    }

    private ResponseEntity<Result<?>> getResponse(int status, ProductErrorCode errorCode) {
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
