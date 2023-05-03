package com.sangeng.service;

import com.sangeng.domain.User;
import com.sangeng.response.ResponseResult;

public interface SystemLoginService {
    ResponseResult login(User user);
}
