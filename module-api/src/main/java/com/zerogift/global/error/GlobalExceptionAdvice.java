package com.zerogift.global.error;

import com.zerogift.global.error.dto.ErrorResultDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ErrorResultDto> globalException(RuntimeException runtimeException) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResultDto.builder()
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .errorDescription(runtimeException.getMessage())
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResultDto> methodArgumentValidException(
        MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest().body(
            new ErrorResultDto(HttpStatus.BAD_REQUEST.getReasonPhrase(), exception.getMessage()));
    }

}
