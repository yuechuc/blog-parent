package com.sangeng.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

    private Long id;

    private String roleName;

    private String roleKey;

    private Integer roleSort;
    private String status;
    private String remark;
    List<Long> menuIds;

}