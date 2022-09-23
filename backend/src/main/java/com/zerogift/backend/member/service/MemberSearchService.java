package com.zerogift.backend.member.service;

import com.zerogift.backend.common.dto.MyPageableDto;
import com.zerogift.backend.member.dto.MemberSearchOutputDto;
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
    public List<MemberSearchOutputDto> searchMemberList(
            SearchMember searchMember,
            MyPageableDto myPageableDto) {

        return memberSearchRepository.searchByWhere(
                searchMember.toCondition(), myPageableDto);
    }

}
