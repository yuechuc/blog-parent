package com.sangeng.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sangeng.domain.Menu;
import com.sangeng.domain.vo.adminVo.AdminMenuVo;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.exception.SystemException;
import com.sangeng.response.ResponseResult;
import com.sangeng.service.MenuService;
import com.sangeng.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;
    @GetMapping("/list")
    public ResponseResult listAllMenu(String status,String menuName){
        return menuService.listAllMenu(status,menuName);
    }

    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu){
        if (Objects.nonNull(menu)){
            menuService.save(menu);
            return ResponseResult.okResult();
        }else {
            throw new SystemException(AppHttpCodeEnum.OPERATION_ERROR);
        }
    }


    //根据id查询菜单数据
    @GetMapping("/{id}")
    public ResponseResult getMenuById(@PathVariable Long id){
        Menu menu = menuService.getById(id);
        AdminMenuVo adminMenuVo = BeanCopyUtils.copyBean(menu, AdminMenuVo.class);
        return ResponseResult.okResult(adminMenuVo);
    }

    //修改菜单
    //能够修改菜单，但是修改的时候不能把父菜单设置为当前菜单，如果设置了需要给出相应的提示。并且修改失败。
    @Transactional
    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu){
        //"修改菜单'写博文'失败，上级菜单不能选择自己"
        if (Objects.nonNull(menu)){
            String parName = menuService.getById(menu.getParentId()).getMenuName();
            if (menu.getMenuName().equals(parName)){
                String errorMessage = "修改菜单" + menu.getMenuName() + "失败，上级菜单不能选择自己";
                throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),errorMessage);
            }

            menuService.updateById(menu);
            return ResponseResult.okResult();
        }else {
            throw new SystemException(AppHttpCodeEnum.OPERATION_ERROR);
        }
    }



    //逻辑删除文章
    @Transactional
    @DeleteMapping("/{menuId}")
    public ResponseResult removeById(@PathVariable Long menuId){
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,menuId);
        List<Menu> menuList = menuService.list(queryWrapper);
        if (menuList == null){
            if (StringUtils.hasText(String.valueOf(menuId))){
                menuService.removeById(menuId);
                return ResponseResult.okResult();
            }else {
                throw new SystemException(AppHttpCodeEnum.OPERATION_ERROR);
            }
        }else {
            throw new SystemException(AppHttpCodeEnum.CHILDREN_NOT_NULL);
        }
    }

    @GetMapping("/treeselect")
    public ResponseResult treeselect(){

        return menuService.treeselect();
    }

}
