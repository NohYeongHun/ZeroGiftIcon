package com.example.demo.dto.common;

import com.example.demo.persist.member.Member;
import lombok.*;

import java.time.Duration;

@NoArgsConstructor
@Data
public class RefreshTokenInputDto {

    @Builder
    @Data
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
