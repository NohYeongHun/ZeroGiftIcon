package com.zerogift.product.presentation;

import com.zerogift.global.error.code.ProductErrorCode;
import com.zerogift.product.application.ProductImageService;
import com.zerogift.support.auth.token.TokenUtil;
import com.zerogift.support.dto.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ProductImageController {
    private final ProductImageService productImageService;

    @PostMapping("admin/upload")
    public ResponseEntity<Result<?>> uploadImages(
        @RequestParam("files") MultipartFile[] request) {
        String email = TokenUtil.getAdminEmail();
        if (email == null) return getResponse(403, ProductErrorCode.INSUFFICIENT_AUTHORITY);
        return ResponseEntity.ok().body(
            Result.builder().data(productImageService.upload(request)).build());
    }

    private ResponseEntity<Result<?>> getResponse(int status, ProductErrorCode errorCode) {
        return ResponseEntity.badRequest().body(
            Result.builder().status(status).success(false).data(errorCode).build());
    }
}
