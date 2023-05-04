package com.sangeng.controller;

import com.sangeng.domain.LoginUser;
import com.sangeng.domain.Menu;
import com.sangeng.domain.vo.AdminUserInfoVo;
import com.sangeng.domain.vo.MenuVo;
import com.sangeng.domain.vo.RoutersVo;
import com.sangeng.domain.vo.UserInfo;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.exception.SystemException;
import com.sangeng.response.ResponseResult;
import com.sangeng.service.MenuService;
import com.sangeng.service.RoleService;
import com.sangeng.service.SystemLoginService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.sangeng.domain.User;

import java.util.List;

@RestController
public class LoginController {

    @Autowired
    private SystemLoginService loginService;
    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){

        if(!StringUtils.hasText(user.getUserName())){
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }


    @GetMapping("/getInfo")
    private ResponseResult getInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Long userId = SecurityUtils.getUserId();
        List<String> perms = menuService.selectPermsByUserId(userId);

        //根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());

        //获取用户信息
        User user = loginUser.getUser();
        UserInfo userInfoVo = BeanCopyUtils.copyBean(user, UserInfo.class);
        //封装数据返回

        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms,roleKeyList,userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }



    @GetMapping("/getRouters")
    private ResponseResult getRouters() {
        Long userId = SecurityUtils.getUserId();

        //查询menu 结果是tree的形式
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);

        RoutersVo routersVo = new RoutersVo(menus);

        return ResponseResult.okResult(routersVo);
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
}
