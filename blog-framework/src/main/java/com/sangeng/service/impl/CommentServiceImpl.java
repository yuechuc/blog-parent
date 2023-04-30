package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.mapper.CommentMapper;
import com.sangeng.response.ResponseResult;
import com.sangeng.service.CommentService;
import com.sangeng.service.UserService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.vo.CommentVo;
import com.sangeng.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sangeng.domian.Comment;


import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-04-30 22:46:57
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {

    @Autowired
    private UserService userService;

    @Override
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Comment::getArticleId,articleId)
                .eq(Comment::getRootId, SystemConstants.COMMENT_ROOT_ID)
                .orderByAsc(Comment::getCreateTime);

        Page<Comment> page = new Page<>(pageNum, pageSize);
        Page<Comment> commentPage = this.page(page, queryWrapper);
        List<Comment> comments = commentPage.getRecords();


        List<CommentVo> commentVos =toCommentVoList(comments);
        PageVo pageVo = new PageVo(commentVos, commentPage.getTotal());

        return ResponseResult.okResult(pageVo);
    }


    private List<CommentVo> toCommentVoList(List<Comment> commentList){
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(commentList, CommentVo.class);
        for (CommentVo commentVo : commentVos) {
            //如果toCommentUserId不为-1才进行查询（回复评论的评论），否则就是直接回复文章的评论
            if (commentVo.getToCommentUserId()!=-1){
                //写入toCommentUserName
                String toCommentUserName =
                        userService
                                .getById(commentVo.getToCommentUserId())
                                .getNickName();
                commentVo.setToCommentUserName(toCommentUserName);
            }

            //写入评论创建人的nickName
            String nickName =
                    userService
                            .getById(commentVo.getCreateBy())
                            .getNickName();
            commentVo.setUserName(nickName);
        }

        return commentVos;
    }
}

