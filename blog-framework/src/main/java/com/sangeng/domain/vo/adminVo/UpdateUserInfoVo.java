package com.sangeng.domain.vo.adminVo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateUserInfoVo {
    private Long id;
    private String userName;
    private String email;
    private String nickName;
    private String sex;
    private Integer status;

}