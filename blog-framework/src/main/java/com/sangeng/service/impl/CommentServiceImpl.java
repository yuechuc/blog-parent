package com.sangeng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.mapper.CommentMapper;
import com.sangeng.response.ResponseResult;
import com.sangeng.service.CommentService;
import org.springframework.stereotype.Service;
import com.sangeng.domian.Comment;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-04-30 22:46:57
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {


    @Override
    public ResponseResult getgetAllLink(Long articleId, Integer pageNum, Integer pageSize) {


        return null;
    }
}

