package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.domain.Menu;
import com.sangeng.domain.vo.adminVo.AdminMenuVo;
import com.sangeng.mapper.MenuMapper;
import com.sangeng.response.ResponseResult;
import com.sangeng.service.MenuService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sangeng.constants.SystemConstants;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-05-03 11:00:01
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<String> selectPermsByUserId(Long userId) {
//如果是管理员，返回所有的权限
        if (SecurityUtils.isAdmin()) {
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
            wrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(wrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        //否则返回所具有的权限
        return getBaseMapper().selectPermsByUserId(userId);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        List<Menu> menus = null;
        if (SecurityUtils.isAdmin()) {
            menus = menuMapper.selectAllRouterMenu();
        } else {
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus,0L);

        return menuTree;
    }

    @Override
    public ResponseResult listAllMenu(String status, String menuName) {
       /*
        需要展示菜单列表，不需要分页。
        可以针对菜单名进行模糊查询
        也可以针对菜单的状态进行查询。
        菜单要按照父菜单id和orderNum进行排序
        */
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper
                .like(StringUtils.hasText(status),Menu::getStatus,status)
                .like(StringUtils.hasText(menuName),Menu::getMenuName,menuName)
                .orderByDesc(Menu::getParentId,Menu::getOrderNum);

        List<Menu> menus = this.list(queryWrapper);
        List<AdminMenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, AdminMenuVo.class);

        return ResponseResult.okResult(menuVos);
    }

    //建立菜单树
    private List<Menu> builderMenuTree(List<Menu> menus, long parentId) {
        List<Menu> menuList = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu->menu.setChildren(getChildren(menu,menus)))
                .collect(Collectors.toList());
        return menuList;
    }

    //获取子菜单
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenLis = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m -> m.setChildren(getChildren(m, menus)))
                .collect(Collectors.toList());
        return childrenLis;

    }
}

