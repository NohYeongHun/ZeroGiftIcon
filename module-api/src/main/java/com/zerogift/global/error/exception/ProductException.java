package com.zerogift.global.error.exception;

import com.zerogift.global.error.code.ProductErrorCode;
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
