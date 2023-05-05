package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.Role;
import com.sangeng.domain.dto.ArticleDto;
import com.sangeng.response.ResponseResult;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-05-03 11:01:29
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult getRoleListByPage(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult changeStatus(Long roleId, String status);
}

