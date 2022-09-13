package com.zerogift.backend.security.dto;

import com.zerogift.backend.member.entity.Member;
import lombok.*;

import java.time.Duration;

@NoArgsConstructor
@Getter
public class RefreshTokenInputDto {

    @Builder
    @Getter
    public static class Info{
        private Member member;
        private String token;
        private Duration expiryDate;

        public static Info of(Member member, String token, Duration expiryDate) {
            return Info.builder()
                    .member(member)
                    .token(token)
                    .expiryDate(expiryDate)
                    .build();
        }
    }



}
