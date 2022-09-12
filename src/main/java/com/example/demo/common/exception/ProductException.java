package com.example.demo.common.exception;

import com.example.demo.common.exception.code.ProductErrorCode;

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
