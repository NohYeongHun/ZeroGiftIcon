package com.zerogift.support.auth.token;

import com.zerogift.member.domain.Member;
import java.time.Duration;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
