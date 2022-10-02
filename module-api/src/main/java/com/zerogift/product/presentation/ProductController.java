package com.zerogift.product.presentation;

import com.zerogift.product.application.ProductService;
import com.zerogift.product.application.ViewHistoryService;
import com.zerogift.product.application.dto.NewProductRequest;
import com.zerogift.product.domain.Category;
import com.zerogift.support.auth.authorization.AuthenticationPrincipal;
import com.zerogift.support.auth.userdetails.LoginInfo;
import com.zerogift.support.dto.Result;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
