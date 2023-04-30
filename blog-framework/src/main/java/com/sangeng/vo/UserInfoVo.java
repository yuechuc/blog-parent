package com.sangeng.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVo {

    private Long id;
    //用户名
    private String userName;
    //昵称
    private String nickName;

    //用户性别（0男，1女，2未知）
    private String sex;
    //邮箱
    private String email;
}
