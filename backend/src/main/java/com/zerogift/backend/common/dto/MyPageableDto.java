package com.zerogift.backend.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ApiModel
public class MyPageableDto {

    @ApiModelProperty(value = "페이지 번호(0~N), default = 0")
    private Integer page;

    @ApiModelProperty(value = "페이지 크기, default = 10")
    private Integer size;

    public MyPageableDto(Integer page, Integer size) {
        this.page = Objects.isNull(page) ? 0 : page;
        this.size = Objects.isNull(size) ? 10 : size;
    }
}

