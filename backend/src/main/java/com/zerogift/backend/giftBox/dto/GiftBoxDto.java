package com.zerogift.backend.giftBox.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class GiftBoxDto {

    private Long id;
    private String name;
    private String imageUrl;
    private String description;
    private boolean use;

    public GiftBoxDto(Long id, String name, String imageUrl, String description, boolean use) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.use = use;
    }
}
