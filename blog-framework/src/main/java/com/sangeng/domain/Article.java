package com.sangeng.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 文章表
 * @TableName sg_article
 */
@TableName(value ="sg_article")
@Data
public class Article implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 文章内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 文章摘要
     */
    @TableField(value = "summary")
    private String summary;

    /**
     * 所属分类id
     */
    @TableField(value = "category_id")
    private Long categoryId;
    @TableField(exist = false)
    private String categoryName;

    /**
     * 缩略图
     */
    @TableField(value = "thumbnail")
    private String thumbnail;

    /**
     * 是否置顶（0否，1是）
     */
    @TableField(value = "is_top")
    private String isTop;

    /**
     * 状态（0已发布，1草稿）
     */
    @TableField(value = "status")
    private String status;

    /**
     * 访问量
     */
    @TableField(value = "view_count")
    private Long viewCount;

    /**
     * 是否允许评论 1是，0否
     */
    @TableField(value = "is_comment")
    private String isComment;

    /**
     * 创建人的用户id
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    @TableField(value = "del_flag")
    private Integer delFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}