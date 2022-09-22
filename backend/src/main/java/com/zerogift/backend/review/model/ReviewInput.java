package com.zerogift.backend.review.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ReviewInput {

    @Max(value = 10, message = "별점은 0 ~ 10점 까지의 정수만 입력 가능합니다.")
    @PositiveOrZero
    @NotBlank
    private Integer rank;  // 별점 점수

    @Size(min = 20, max = 1000, message = "리뷰 내용은 20 ~ 1000자 사이로 입력해주십시오.")
    @NotBlank(message = "내용은 필수 항목입니다.")
    private String description;
}
