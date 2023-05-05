package com.sangeng.domain.vo.adminVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
    private Long id;
    private String userName;
    private String nickName;
    private String phonenumber;
    private String status;
    private Date createTime;
}