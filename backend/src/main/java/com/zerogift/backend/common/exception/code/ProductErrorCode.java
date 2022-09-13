package com.zerogift.backend.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ProductErrorCode {
    CATEGORY_NOT_FOUND("카테고리를 찾을 수 없습니다."),
    INSUFFICIENT_AUTHORITY("상품 등록 권한이 없습니다."),
    PRODUCT_NOT_FOUND("상품을 찾을 수 없습니다."),
    OWNED_BY_SOMEONE_ELSE("상품을 삭제할 권한이 없습니다."),
    VOTE_NOT_ALLOWED_FOR_NON_MEMBER("좋아요는 회원만 할 수 있습니다"),
    SELF_VOTE_FORBIDDEN("본인 상품에 좋아요를 할 수 없습니다."),
    DUPLICATE_VOTING_FORBIDDEN("이미 좋아요 한 상품에 다시 좋아요 할 수 없습니다.");
    private String description;
}
