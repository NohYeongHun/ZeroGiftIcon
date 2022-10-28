package com.zerogift.member.application;

import com.zerogift.member.application.dto.MemberSearchDetail;
import com.zerogift.member.application.dto.MemberSearchOutputDto;
import com.zerogift.member.application.dto.MemberSearchInfo;
import com.zerogift.member.application.dto.MemberSearchRequest;
import com.zerogift.support.dto.MyPageableDto;
import com.zerogift.member.repository.MemberSearchRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberSearchService {

    private final MemberSearchRepository memberSearchRepository;

    @Transactional
    public MemberSearchInfo searchMemberList(
        MemberSearchRequest searchMember,
        MyPageableDto myPageableDto) {

        List<MemberSearchOutputDto> memberSearchOutputDtoList = memberSearchRepository
            .searchMemberList(searchMember.toCondition(), myPageableDto);
        Long totalCount = memberSearchRepository.getTotalCount(searchMember.toCondition());
        Integer pageSize = myPageableDto.getSize();
        Integer totalPage = Double.valueOf(Math.ceil(Double.valueOf(totalCount) / myPageableDto.getSize())).intValue();

        return MemberSearchInfo
            .builder()
            .memberSearchOutputDtoList(memberSearchOutputDtoList)
            .totalPage(totalPage)
            .page(myPageableDto.getPage())
            .size(pageSize)
            .build();
    }

    @Transactional
    public MemberSearchDetail searchMemberDetail(Long memberId) {
        return memberSearchRepository.searchMemberDetail(memberId);
    }

}
