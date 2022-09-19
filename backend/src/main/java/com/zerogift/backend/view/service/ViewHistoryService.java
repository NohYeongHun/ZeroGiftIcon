package com.zerogift.backend.view.service;

import com.zerogift.backend.security.dto.LoginInfo;

public interface ViewHistoryService {

    // 조회수 증가
    void addViewHistory(LoginInfo loginInfo, Long productId);
}
