package com.zerogift.backend.member.service;

import com.zerogift.backend.common.dto.MyPageableDto;
import com.zerogift.backend.member.dto.MemberSearchOutputDto;
import com.zerogift.backend.member.dto.MemberSearchOutputPageDto;
import com.zerogift.backend.member.dto.SearchMember;
import com.zerogift.backend.member.repository.MemberSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberSearchService {

    private final MemberSearchRepository memberSearchRepository;


    @Transactional
    public MemberSearchOutputPageDto searchMemberList(
            SearchMember searchMember,
            MyPageableDto myPageableDto) {

        List<MemberSearchOutputDto> memberSearchOutputDtoList = memberSearchRepository
                .searchByWhere(searchMember.toCondition(), myPageableDto);
        Long totalCount = memberSearchRepository.getTotalCount(searchMember.toCondition());
        Integer pageSize = myPageableDto.getSize();
        Integer totalPage = Double.valueOf(Math.ceil(Double.valueOf(totalCount) / myPageableDto.getSize())).intValue();

        return MemberSearchOutputPageDto
                .builder()
                .memberSearchOutputDtoList(memberSearchOutputDtoList)
                .totalPage(totalPage)
                .page(myPageableDto.getPage())
                .size(pageSize)
                .build();
    }

}
