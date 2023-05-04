package com.sangeng.domain.vo.adminVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminArticleListVo {

    private String title;
    private String content;
    private String summary;
    private Long categoryId;
    private String thumbnail;
    private String isTop;
    private String status;
    private String isComment;
    private Long id;
    private Date createTime;
    private Long viewCount;

}
