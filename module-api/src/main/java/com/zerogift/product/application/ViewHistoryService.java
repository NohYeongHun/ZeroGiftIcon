package com.zerogift.product.application;

import com.zerogift.support.auth.userdetails.LoginInfo;

public interface ViewHistoryService {

    // 조회수 증가
    void addViewHistory(LoginInfo loginInfo, Long productId);
}
