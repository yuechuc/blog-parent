package com.sangeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sangeng.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-03 10:54:02
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<Long> getRoleIdsByUserId(Long id);
}

