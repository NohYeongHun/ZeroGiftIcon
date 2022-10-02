package com.zerogift.global.error.code;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PayErrorCode {
    NOT_ENOUGH_POINT("사용 포인트가 보유 포인트보다 많습니다."),
    NOT_ENOUGH_PRODUCT_COUNT("상품이 품절 상태입니다."),
    REDISSON_EXCEPTION("레디스 락 걸릴떄, 에러가 발생하였습니다.");

    private final String description;
}
