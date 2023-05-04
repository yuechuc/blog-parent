package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.domain.dto.TagDto;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.mapper.TagMapper;
import com.sangeng.response.ResponseResult;
import com.sangeng.service.TagService;
import org.springframework.stereotype.Service;
import com.sangeng.domain.Tag;
import org.springframework.util.StringUtils;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-05-02 15:46:39
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagDto tagDto) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(StringUtils.hasText(tagDto.getName()),Tag::getName,tagDto.getName())
                .eq(StringUtils.hasText(tagDto.getRemark()),Tag::getRemark,tagDto.getRemark())
                .orderByDesc(Tag::getUpdateTime);
        Page<Tag> page  = new Page<>(pageNum, pageSize);
        Page<Tag> tagPage = this.page(page, queryWrapper);

        PageVo pageVo = new PageVo(tagPage.getRecords(), tagPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }
}

