package com.sangeng.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentVo {
    private Long id;
    //文章id
    private Long articleId;
    //根评论id
    private Long rootId;
    //评论内容
    private String content;
    //所回复的目标评论的userid
    private Long toCommentUserId;
    //实际上是取nickName
    private String toCommentUserName;
    //回复目标评论id
    private Long toCommentId;

    //评论创建人的id
    private Long createBy;

    //评论创建人的nickName
    private String userName;

    private Date createTime;

    private List<CommentVo> Children;

}
