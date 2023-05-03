package com.sangeng.service.impl;

import com.sangeng.domain.LoginUser;
import com.sangeng.domain.User;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.exception.SystemException;
import com.sangeng.response.ResponseResult;
import com.sangeng.service.BlogLoginService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.JwtUtil;
import com.sangeng.utils.RedisCache;
import com.sangeng.domain.vo.BlogUserLoginVo;
import com.sangeng.domain.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.sangeng.constants.RedisKey.BLOG_LOGIN;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if (Objects.isNull(authenticate)) {
            throw new SystemException(AppHttpCodeEnum.LOGIN_ERROR);
        }
        //获取userid 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        Long userId = loginUser.getUser().getId();
        String token = JwtUtil.createJWT(String.valueOf(userId));
        //把用户信息存入redis
        redisCache.setCacheObject(BLOG_LOGIN+userId,loginUser);

        //把User转换成UserInfoVo
        UserInfo userInfo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfo.class);
        //把token和userinfo封装blogUserLoginVo返回
        BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(token, userInfo);
        return ResponseResult.okResult(blogUserLoginVo);
    }

    @Override
    public ResponseResult logout() {
        //获取token 解析获取userid
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //获取userid
        Long userId = loginUser.getUser().getId();
        //删除redis中的用户信息
        redisCache.deleteObject(BLOG_LOGIN+userId);
        return ResponseResult.okResult();
    }
}
