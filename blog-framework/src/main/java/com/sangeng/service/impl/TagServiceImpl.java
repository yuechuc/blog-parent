package com.sangeng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.mapper.TagMapper;
import com.sangeng.service.TagService;
import org.springframework.stereotype.Service;
import com.sangeng.domain.Tag;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-05-02 15:46:39
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

}

