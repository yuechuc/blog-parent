package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.domain.LoginUser;
import com.sangeng.domain.Role;
import com.sangeng.domain.UserRole;
import com.sangeng.domain.dto.AdminUserDto;
import com.sangeng.domain.dto.UserDto;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.domain.vo.adminVo.UpdateUserInfoVo;
import com.sangeng.domain.vo.adminVo.UpdateUserVo;
import com.sangeng.domain.vo.adminVo.UserVo;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.exception.SystemException;
import com.sangeng.mapper.UserMapper;
import com.sangeng.response.ResponseResult;
import com.sangeng.service.UserRoleService;
import com.sangeng.service.UserService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.domain.vo.UserInfo;
import com.sangeng.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.sangeng.domain.User;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleService userRoleService;
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

    @Override
    public ResponseResult updateUserInfo(UserDto userDto) {
        User user = BeanCopyUtils.copyBean(userDto, User.class);
        this.updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }


        //对数据进行是否存在的判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if(emailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //对密码进行加密
        String encodePW = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePW);
        //存入数据库
        save(user);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserListByPage(Integer pageNum, Integer pageSize, AdminUserDto adminUserDto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Strings.isNotEmpty(adminUserDto.getUserName()),User::getUserName,adminUserDto.getUserName())
                .eq(Strings.isNotEmpty(adminUserDto.getPhonenumber()),User::getPhonenumber,adminUserDto.getPhonenumber())
                .eq(Strings.isNotEmpty(adminUserDto.getStatus()),User::getStatus,adminUserDto.getStatus())
                .orderByAsc(User::getCreateTime);
        Page<User> pageObj = new Page<>(pageNum,pageSize);
        pageObj= page(pageObj, wrapper);
        List<User> users = pageObj.getRecords();
        List<UserVo> userVos = BeanCopyUtils.copyBeanList(users, UserVo.class);
        PageVo pageVo = new PageVo(userVos,pageObj.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addUser(AdminUserDto adminUserDto) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(User::getUserName, adminUserDto.getUserName())
                .or()
                .eq(User::getEmail, adminUserDto.getEmail())
                .or()
                .eq(User::getPhonenumber, adminUserDto.getPhonenumber());

        List<User> userList = list(queryWrapper);
        for (User user : userList) {
            if (user.getUserName().equals(adminUserDto.getUserName())) {
                // 用户名重复
                throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
            } else if (user.getEmail().equals(adminUserDto.getEmail())) {
                // 邮箱重复
                throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
            } else if (user.getPhonenumber().equals(adminUserDto.getPhonenumber())) {
                // 手机号重复
                throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
            }
        }

        adminUserDto.setPassword(passwordEncoder.encode(adminUserDto.getPassword()));
        User user=BeanCopyUtils.copyBean(adminUserDto,User.class);
        save(user);
        List<Long> roleIds = adminUserDto.getRoleIds();
        for (Long roleId : roleIds) {
            UserRole userRole = new UserRole(user.getId(), roleId);
            userRoleService.save(userRole);
        }

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteUserById(Long id) {
        if (SecurityUtils.getUserId().equals(id)){
            throw new SystemException(AppHttpCodeEnum.CAN_NOT_DELETE_LOGIN_USER);
        }
        removeById(id);

        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,id);
        userRoleService.remove(queryWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public UpdateUserVo getUserInfoById(Long id) {
        User user = getById(id);
        UpdateUserInfoVo updateUserInfoVo = BeanCopyUtils.copyBean(user, UpdateUserInfoVo.class);
        UpdateUserVo updateUserVo = new UpdateUserVo();
        updateUserVo.setUser(updateUserInfoVo);
        List<Long> roleIds = baseMapper.getRoleIdsByUserId(id);
        updateUserVo.setRoleIds(roleIds);
        return updateUserVo;
    }

    @Override
    public ResponseResult updateUser(AdminUserDto userDto) {
        //待优化
        User nowUser = getById(userDto.getId());
        if (!nowUser.getUserName().equals(userDto.getUserName())&&userNameExist(userDto.getUserName()))
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        if (!nowUser.getEmail().equals(userDto.getEmail())&&emailExist(userDto.getEmail()))
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        User user=BeanCopyUtils.copyBean(userDto,User.class);
        updateById(user);

        LambdaQueryWrapper<UserRole> userRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userRoleLambdaQueryWrapper.eq(UserRole::getUserId,user.getId());
        userRoleService.remove(userRoleLambdaQueryWrapper);
        List<Long> roleIds = userDto.getRoleIds();
        for (Long roleId : roleIds) {
            userRoleService.save(new UserRole(user.getId(),roleId));
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeStatus(String status, Long id) {
        User user = getById(id);
        if (Objects.isNull(user)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.USER_NOT_EXIST);
        }
        user.setStatus(status);
        updateById(user);
        return ResponseResult.okResult();
    }

    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email);

        int count = this.count(queryWrapper);
        return count>0;
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,nickName);

        int count = this.count(queryWrapper);
        return count>0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);

        int count = this.count(queryWrapper);
        return count>0;
    }
}

