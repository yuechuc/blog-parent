package com.sangeng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.mapper.UserMapper;
import com.sangeng.response.ResponseResult;
import com.sangeng.service.UserService;
import org.springframework.stereotype.Service;
import com.sangeng.domian.User;
/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-04-30 19:28:00
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
}

