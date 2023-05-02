package com.sangeng.controller;

import com.sangeng.annotation.SystemLog;
import com.sangeng.domain.dto.UserDto;
import com.sangeng.response.ResponseResult;
import com.sangeng.service.LinkService;
import com.sangeng.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.sangeng.domian.User;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/userInfo")
    public ResponseResult userInfo(){
        return  userService.userInfo();
    }

    @SystemLog(bussinessName = "更新用户信息")
    @PutMapping("/userInfo")
    public ResponseResult updateUserInfo(@RequestBody UserDto userDto){
        return  userService.updateUserInfo(userDto);
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
        return  userService.register(user);
    }
}



