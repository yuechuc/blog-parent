package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.Article;
import com.sangeng.response.ResponseResult;

public interface ArticleService extends IService<Article> {
    ResponseResult getHotArticleList();
}
