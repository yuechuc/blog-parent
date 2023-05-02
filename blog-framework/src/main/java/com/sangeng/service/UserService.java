package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.dto.UserDto;
import com.sangeng.domian.User;
import com.sangeng.response.ResponseResult;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-04-30 19:28:00
 */
public interface UserService extends IService<User> {


    ResponseResult userInfo();

    ResponseResult updateUserInfo(UserDto userDto);

    ResponseResult register(User user);
}

