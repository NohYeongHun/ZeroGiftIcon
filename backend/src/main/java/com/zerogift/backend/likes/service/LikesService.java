package com.zerogift.backend.likes.service;

import com.zerogift.backend.common.dto.Result;
import com.zerogift.backend.security.dto.LoginInfo;
import org.springframework.http.ResponseEntity;

public interface LikesService {
    // 좋아요 누르기
    ResponseEntity<Result<?>> pressLike(LoginInfo loginInfo, Long productId);

    // 좋아요 취소하기
    ResponseEntity<Result<?>> likeCancel(LoginInfo loginInfo, Long productId);

    // 좋아요 누른 리스트
    ResponseEntity<?> likeList(LoginInfo loginInfo);
}
