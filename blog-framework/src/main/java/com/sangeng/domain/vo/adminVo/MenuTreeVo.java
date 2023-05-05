package com.sangeng.domain.vo.adminVo;

import com.sangeng.domain.Menu;
import com.sangeng.domain.vo.MenuVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuTreeVo {
    private Long id;
    private String label;
    private Long parentId;
    private List<? extends MenuTreeVo> children;
}