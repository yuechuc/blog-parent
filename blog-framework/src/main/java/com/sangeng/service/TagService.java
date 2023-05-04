package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.Tag;
import com.sangeng.domain.dto.TagDto;
import com.sangeng.response.ResponseResult;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-05-02 15:46:39
 */
public interface TagService extends IService<Tag> {

    ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagDto tagListDto);
}

