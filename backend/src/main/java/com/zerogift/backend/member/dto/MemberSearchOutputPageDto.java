package com.zerogift.backend.member.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSearchOutputPageDto {

    private List<MemberSearchOutputDto> memberSearchOutputDtoList;
    private Integer totalPage;
    private Integer page;
    private Integer size;

    @Builder
    public MemberSearchOutputPageDto(
            List<MemberSearchOutputDto> memberSearchOutputDtoList,
            Integer totalPage,
            Integer page,
            Integer size) {
        this.memberSearchOutputDtoList = memberSearchOutputDtoList;
        this.totalPage = totalPage;
        this.page = page;
        this.size = size;
    }

}
