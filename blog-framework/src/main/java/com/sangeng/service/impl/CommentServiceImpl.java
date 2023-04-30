package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.exception.SystemException;
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
import org.springframework.util.StringUtils;


import java.util.List;
import java.util.Objects;

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
                .orderByDesc(Comment::getCreateTime);

        Page<Comment> page = new Page<>(pageNum, pageSize);
        Page<Comment> commentPage = this.page(page, queryWrapper);
        List<Comment> comments = commentPage.getRecords();
        List<CommentVo> commentVos =toCommentVoList(comments);

        //查询子评论
        for (CommentVo commentVo : commentVos) {
            List<CommentVo> chidren = getChidren( commentVo.getRootId());
            commentVo.setChildren(chidren);
        }
        PageVo pageVo = new PageVo(commentVos, commentPage.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        if (Objects.isNull(comment)){
            throw new SystemException(AppHttpCodeEnum.CONTEXT_NOT_NULL);
        }
        this.save(comment);
        return ResponseResult.okResult();
    }


    //根据根评论的id查询所对应的子评论的集合
   //rootId 根评论的id
    private List<CommentVo> getChidren(Long rootId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Comment::getRootId,rootId)
                .orderByDesc(Comment::getCreateTime);
        List<Comment> list = this.list(queryWrapper);
        List<CommentVo> commentVos = toCommentVoList(list);
        return commentVos;
    }


    //写入被回复评论的nickName
    //和评论创建人的nickName
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

