package com.zerogift.backend.likes.service;

import com.zerogift.backend.common.dto.Result;
import com.zerogift.backend.likes.entity.Likes;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LikesService {
    // 좋아요 누르기
    ResponseEntity<Result<?>> pressLike(String email, Long productId);

    // 좋아요 취소하기
    ResponseEntity<Result<?>> likeCancel(String email, Long productId);

    // 좋아요 누른 리스트
    ResponseEntity<?> likeList(String email);
}
