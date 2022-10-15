package com.zerogift.product.application;

import com.zerogift.product.application.dto.LikesModel;
import com.zerogift.support.auth.userdetails.LoginInfo;
import com.zerogift.support.dto.Result;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface LikesService {
    // 좋아요 누르기
    ResponseEntity<Result<?>> pressLike(LoginInfo loginInfo, Long productId);

    // 좋아요 누른 리스트
    List<LikesModel> likeList(LoginInfo loginInfo);
}
