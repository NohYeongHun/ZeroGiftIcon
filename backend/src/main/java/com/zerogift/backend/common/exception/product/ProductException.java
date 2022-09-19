package com.zerogift.backend.common.exception.product;

import com.zerogift.backend.common.exception.code.ProductErrorCode;

import lombok.Getter;

@Getter
public class ProductException extends RuntimeException {
    private final ProductErrorCode errorCode;
    private final String errorMessage;

    public ProductException(ProductErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
