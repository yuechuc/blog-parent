package com.sangeng.controller;

import com.sangeng.domain.dto.ArticleDto;
import com.sangeng.domain.dto.RoleDto;
import com.sangeng.domain.dto.RoleStatusDto;
import com.sangeng.domain.vo.adminVo.RoleVo;
import com.sangeng.response.ResponseResult;
import com.sangeng.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    RoleService roleService;
    @GetMapping("/list")
    public ResponseResult listByPage(Integer pageNum,Integer pageSize, String roleName, String status) {
        return roleService.getRoleListByPage(pageNum,pageSize, roleName, status);
    }
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody RoleStatusDto roleStatusDto) {
        return roleService.changeStatus(roleStatusDto.getRoleId(), roleStatusDto.getStatus());
    }
    @PostMapping
    public ResponseResult addRole(@RequestBody RoleDto roleDto) {
        return roleService.addRole(roleDto);
    }
    @GetMapping("/{id}")
    public ResponseResult<RoleVo> getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id);
    }
    @PutMapping
    public ResponseResult updateRole(@RequestBody RoleDto roleDto) {
        return roleService.updateRole(roleDto);
    }
    //@DeleteMapping("/{id}")
    //public ResponseResult<Object> deleteRole(@PathVariable Long id) {
    //    return roleService.deleteRoleById(id);
    //}
    //@GetMapping("/listAllRole")
    //public ResponseResult<List<SimpleRoleVo>> listAllRole() {
    //    return roleService.getAllRole();
    //}
}
