package com.zerogift.batch.core.entity.product;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Category {
    BIRTHDAY("생일"),
    CLOTH("의류"),
    COSMETIC("화장품"),
    DELIVERY("배송"),
    FOOD("음식"),
    GIFTCARD("기프트카드"),
    HEALTH("건강"),
    LUXURY("명품"),
    OTHER("기타");

    private final String description;

}
