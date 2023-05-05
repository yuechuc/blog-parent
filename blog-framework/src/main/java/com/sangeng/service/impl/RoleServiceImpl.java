package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.Article;
import com.sangeng.domain.Menu;
import com.sangeng.domain.Role;
import com.sangeng.domain.RoleMenu;
import com.sangeng.domain.dto.ArticleDto;
import com.sangeng.domain.dto.RoleDto;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.domain.vo.adminVo.AdminArticleListVo;
import com.sangeng.domain.vo.adminVo.RoleVo;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.mapper.RoleMapper;
import com.sangeng.response.ResponseResult;
import com.sangeng.service.RoleMenuService;
import com.sangeng.service.RoleService;
import com.sangeng.utils.BeanCopyUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-05-03 11:01:29
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员 如果是返回集合中只需要有admin
        if (id == 1L) {
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //否则查询用户所具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult getRoleListByPage(Integer pageNum, Integer pageSize, String roleName, String status) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Strings.isNotEmpty(roleName), Role::getRoleName, roleName)
                .eq(Strings.isNotEmpty(status), Role::getStatus, status)
                .orderByAsc(Role::getRoleSort);
        Page<Role> pageObj = new Page<>(pageNum, pageSize);
        pageObj = page(pageObj, queryWrapper);
        List<Role> roleList = pageObj.getRecords();
        List<RoleVo> roleVoList = BeanCopyUtils.copyBeanList(roleList, RoleVo.class);
        PageVo pageVo = new PageVo(roleVoList, pageObj.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult changeStatus(Long roleId, String status) {
        Role role = getById(roleId);
        if (Objects.isNull(role)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.ROLE_NOT_EXIT);
        }
        role.setStatus(status);
        updateById(role);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addRole(RoleDto roleDto) {

        Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
        save(role);
        List<Long> menuIds = roleDto.getMenuIds();

        if (menuIds != null && menuIds.size() > 0) {
            for (Long menuId : menuIds) {
                roleMenuService.save(new RoleMenu(role.getId(), menuId));
            }
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<RoleVo> getRoleById(Long id) {
        Role role = getById(id);
        if (Objects.isNull(role)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.ROLE_NOT_EXIT);
        }
        RoleVo roleVo = BeanCopyUtils.copyBean(role, RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    @Transactional
    @Override
    public ResponseResult updateRole(RoleDto roleDto) {
        Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
        updateById(role);
        List<Long> menuIds = roleDto.getMenuIds();
        if (menuIds != null && menuIds.size() > 0) {
            //baseMapper.deleteRoleMenuByRoleId(role.getId());
            //baseMapper.insertRoleMenu(menuIds, role.getId());
            LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(RoleMenu::getRoleId, role.getId());
            roleMenuService.remove(queryWrapper);
            for (Long menuId : menuIds) {
                roleMenuService.save(new RoleMenu(role.getId(), menuId));
            }

        } else {
            roleMenuService.removeById(role.getId());
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteRoleById(Long id) {
        if (id == SystemConstants.ADMAIN_ID) {
            return ResponseResult.errorResult(AppHttpCodeEnum.CAN_NOT_DELETE_ADMIN);
        }
        roleMenuService.removeById(id);
        removeById(id);
        return ResponseResult.okResult();
    }


}

