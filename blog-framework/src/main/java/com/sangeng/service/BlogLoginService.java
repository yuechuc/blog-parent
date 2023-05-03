package com.sangeng.service;

import com.sangeng.response.ResponseResult;
import com.sangeng.domain.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
