package com.sangeng.controller;

import com.sangeng.response.ResponseResult;
import com.sangeng.service.BlogLoginService;
import com.sangeng.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sangeng.domian.User;

@RestController
@RequestMapping("/login")
public class BlogLoginController {
    @Autowired
    private BlogLoginService blogLoginService;
    @PostMapping
    public ResponseResult login(@RequestBody User user){
     return blogLoginService.login(user);
    }
}
