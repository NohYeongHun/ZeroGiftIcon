package com.zerogift.global.error.code;

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
    NON_EXISTENT_IMAGE_ID("해당 ID의 이미지가 존재하지 않습니다"),
    PRIVATE_PRODUCT("비공개 상품은 주인만 조회할 수 있습니다"),

    VOTE_NOT_ALLOWED_FOR_NON_MEMBER("좋아요는 회원만 할 수 있습니다"),
    SELF_VOTE_FORBIDDEN("본인 상품에 좋아요를 할 수 없습니다."),
    DUPLICATE_VOTING_FORBIDDEN("이미 좋아요 한 상품에 다시 좋아요 할 수 없습니다."),
    NEVER_PRESS_LIKE("해당 상품을 좋아요를 누른적이 없습니다"),

    SELF_VIEW_NOT_COUNT("본인의 상품을 조회해도 조회수는 오르지 않습니다."),
    ALREADY_VIEW_PRODUCT("이미 조회한 상품입니다.");

    private String description;
}