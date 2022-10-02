package com.zerogift.notice.domain;

import lombok.Getter;

public enum NoticeType {
    review("리뷰를 등록하였습니다."),
    gift("선물하였습니다."),
    message("감사메세지를 작성하였습니다.");

    @Getter
    private final String description;

    NoticeType( String description) {
        this.description = description;
    }

}
