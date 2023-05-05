package com.sangeng.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sangeng.domain.Tag;
import com.sangeng.domain.User;
import com.sangeng.domain.dto.AdminUserDto;
import com.sangeng.domain.dto.ArticleDto;
import com.sangeng.domain.dto.TagDto;
import com.sangeng.domain.dto.UserDto;
import com.sangeng.domain.vo.adminVo.RoleVo;
import com.sangeng.domain.vo.adminVo.SimpleRoleVo;
import com.sangeng.domain.vo.adminVo.UpdateUserVo;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.exception.SystemException;
import com.sangeng.response.ResponseResult;
import com.sangeng.service.RoleService;
import com.sangeng.service.UserService;
import com.sangeng.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseResult getUserList(Integer pageNum, Integer pageSize, AdminUserDto adminUserDto) {
        return userService.getUserListByPage(pageNum, pageSize, adminUserDto);
    }

    @PostMapping
    public ResponseResult addUser(@RequestBody AdminUserDto adminUserDto) {
        return userService.addUser(adminUserDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteUser(@PathVariable Long id) {
        return userService.deleteUserById(id);
    }

    @GetMapping("/{id}")
    public ResponseResult<UpdateUserVo> getUserInfo(@PathVariable Long id) {
        UpdateUserVo userVo = userService.getUserInfoById(id);
        ResponseResult<List<SimpleRoleVo>> roles = roleService.getAll();
        userVo.setRoles(roles.getData());
        return ResponseResult.okResult(userVo);
    }

    @Transactional
    @PutMapping
    public ResponseResult updateUserInfo(@RequestBody AdminUserDto userDto) {
        return userService.updateUser(userDto);
    }
}
