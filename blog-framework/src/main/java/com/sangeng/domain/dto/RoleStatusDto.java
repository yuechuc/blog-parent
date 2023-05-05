package com.sangeng.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleStatusDto {
    private Long roleId;
    private String status;
}