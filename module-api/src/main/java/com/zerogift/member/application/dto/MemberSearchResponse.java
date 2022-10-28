package com.zerogift.member.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MemberSearchResponse {

    private final List<MemberSearchOutputDto> memberSearchOutputDtoList;
    private final Integer totalPage;
    private final Integer page;
    private final Integer size;

    @Builder
    public MemberSearchResponse(
            List<MemberSearchOutputDto> memberSearchOutputDtoList,
            Integer totalPage,
            Integer page,
            Integer size
    ) {
        this.memberSearchOutputDtoList = memberSearchOutputDtoList;
        this.totalPage = totalPage;
        this.page = page;
        this.size = size;
    }

    public static MemberSearchResponse from(MemberSearchInfo memberSearchInfo){

        return MemberSearchResponse.builder()
                .memberSearchOutputDtoList(memberSearchInfo.getMemberSearchOutputDtoList())
                .totalPage(memberSearchInfo.getTotalPage())
                .page(memberSearchInfo.getPage())
                .size(memberSearchInfo.getSize())
                .build();

    }

}
