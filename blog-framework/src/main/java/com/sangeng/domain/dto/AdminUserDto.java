package com.sangeng.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserDto {
        private Long id;
        private String userName;
        private String nickName;
        private String password;
        private String status;
        private String email;
        private String phonenumber;
        private String sex;
        //头像
        private List<Long> roleIds;
}
