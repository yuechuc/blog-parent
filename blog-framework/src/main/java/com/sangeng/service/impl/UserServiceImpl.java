package com.sangeng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.domain.LoginUser;
import com.sangeng.mapper.UserMapper;
import com.sangeng.response.ResponseResult;
import com.sangeng.service.UserService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.vo.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.sangeng.domian.User;
/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-04-30 19:28:00
 */
@Service("userService")
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Override
    public ResponseResult userInfo() {
        //获取当前用户id
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = loginUser.getUser().getId();
        User user = this.getById(userId);
        //封装成UserInfoVo
        UserInfo userInfo = BeanCopyUtils.copyBean(user, UserInfo.class);
        return ResponseResult.okResult(userInfo);
    }
}

