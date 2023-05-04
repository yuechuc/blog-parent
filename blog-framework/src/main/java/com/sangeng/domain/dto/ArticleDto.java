package com.sangeng.domain.dto;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto implements Serializable {

    private String title;

    private String content;


    private String summary;


    private Long categoryId;

    private String thumbnail;

    private String isTop;

    /**
     * 状态（0已发布，1草稿）
     */
    private String status;


    private String isComment;

    List<Integer> tags;
}