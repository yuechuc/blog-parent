package com.sangeng.service;

import com.sangeng.response.ResponseResult;
import com.sangeng.domian.User;

public interface BlogLoginService {
    ResponseResult login(User user);
}
