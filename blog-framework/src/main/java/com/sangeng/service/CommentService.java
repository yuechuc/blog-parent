package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domian.Comment;
import com.sangeng.response.ResponseResult;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-04-30 22:42:31
 */
public interface CommentService extends IService<Comment> {



    ResponseResult commentList(String commentTpye, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);

}

