package com.zerogift.support.dto;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MyPageableDto {

    private Integer page;

    private Integer size;

    public MyPageableDto(Integer page, Integer size) {
        this.page = Objects.isNull(page) ? 0 : page;
        this.size = Objects.isNull(size) ? 10 : size;
    }

}
