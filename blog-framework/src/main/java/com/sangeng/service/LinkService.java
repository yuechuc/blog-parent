package com.sangeng.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.Category;
import com.sangeng.domain.Link;
import com.sangeng.domain.dto.LinkQueryDto;
import com.sangeng.response.ResponseResult;

/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-04-30 18:59:11
 */

public interface LinkService extends IService<Link> {

    ResponseResult getgetAllLink();

    ResponseResult list(Integer pageNum, Integer pageSize, LinkQueryDto linkQueryDto);

    ResponseResult changeStatus(Long id, String status);
}

