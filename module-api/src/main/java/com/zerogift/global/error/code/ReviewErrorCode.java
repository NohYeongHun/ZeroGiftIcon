package com.zerogift.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ReviewErrorCode {
    REVIEW_NOT_FOUND(" 해당 리뷰를 찾을 수 없습니다. "),
    ADD_REVIEW_AFTER_USE("선물을 사용 후 리뷰를 작성해 주십시오.");

    private String description;
}
