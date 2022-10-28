package com.zerogift.member.application.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSearchInfo {

    private List<MemberSearchOutputDto> memberSearchOutputDtoList;
    private Integer totalPage;
    private Integer page;
    private Integer size;

    @Builder
    public MemberSearchInfo(
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
