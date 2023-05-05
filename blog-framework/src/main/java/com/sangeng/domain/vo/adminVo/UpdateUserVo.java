package com.sangeng.domain.vo.adminVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserVo {
    private UpdateUserInfoVo user;
    private List<Long> roleIds;
    private List<SimpleRoleVo> roles;
}