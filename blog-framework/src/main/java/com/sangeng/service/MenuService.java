package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.Menu;
import com.sangeng.response.ResponseResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-05-03 11:00:01
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(@Param("userId") Long userId);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    ResponseResult listAllMenu(String status, String menuName);
}

