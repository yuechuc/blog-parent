package com.sangeng.domain.vo;

import com.sangeng.domain.vo.adminVo.MenuTreeVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuVo  {

    List<MenuVo> children;
    private String component;
    private Date createTime;
    private String icon;
    private Long id;
    private String menuName;
    private String menuType;
    private Integer orderNum;
    private Long parentId;
    private String path;
    private String perms;
    private String status;
    private String visible;

}
