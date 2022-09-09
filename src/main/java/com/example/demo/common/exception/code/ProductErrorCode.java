package com.example.demo.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ProductErrorCode {
    CATEGORY_NOT_FOUND("카테고리를 찾을 수 없습니다."),
    INSUFFICIENT_AUTHORITY("상품 등록 권한이 없습니다."),
    PRODUCT_NOT_FOUND("상품을 찾을 수 없습니다");
    private String description;
}
