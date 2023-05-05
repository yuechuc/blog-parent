package com.sangeng.domain.vo.adminVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminMenuVo {
    private Long id;
    private String menuName;
    private Long parentId;
    private Integer orderNum;
    private String path;
    private String component;
    private Integer isFrame;
    private String menuType;
    private String status;
    private String perms;
    private String icon;
    private String remark;
    private String visible;
    private Date createTime;

}
