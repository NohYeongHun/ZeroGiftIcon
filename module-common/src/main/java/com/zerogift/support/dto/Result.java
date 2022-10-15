package com.zerogift.support.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Result<T> {
    @Builder.Default
    private Integer status = 200;
    @Builder.Default
    private Boolean success = true;
    private T data;
}