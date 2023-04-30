package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.Article;
import com.sangeng.domain.Category;
import com.sangeng.mapper.CategoryMapper;
import com.sangeng.response.ResponseResult;
import com.sangeng.service.ArticleService;
import com.sangeng.mapper.ArticleMapper;
import com.sangeng.service.CategoryService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.vo.ArticleListVo;
import com.sangeng.vo.HotArticleVo;
import com.sangeng.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.sangeng.constants.SystemConstants.ARTICLE_STATUS_NORMAL;

/**
 * @author yuechu
 * @description 针对表【sg_article(文章表)】的数据库操作Service实现
 * @createDate 2023-04-29 23:25:47
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
        implements ArticleService {

    @Autowired
    private CategoryService categoryService;

    @Override
    public ResponseResult getHotArticleList() {
        //查询热门文章 封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章
        queryWrapper.eq(Article::getStatus, ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多只查询10条
        Page<Article> page = new Page(1, 10);
        page(page, queryWrapper);

        List<Article> articles = page.getRecords();
        //bean拷贝
        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);

        return ResponseResult.okResult(hotArticleVos);
    }

    @Override
    public ResponseResult getArticleList(Integer pageNum, Integer pageSize, Long categoryId) {
        Page<Article> articlePage = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0, Article::getCategoryId, categoryId);

        queryWrapper.eq(Article::getStatus, ARTICLE_STATUS_NORMAL);

        queryWrapper.orderByDesc(Article::getIsTop).orderByDesc(Article::getUpdateTime);
        IPage<Article> page = this.page(articlePage, queryWrapper);

        List<Article> articles = page.getRecords();
        for (Article article : articles) {
            Category category = categoryService.getById(article.getCategoryId());
            article.setCategoryName(category.getName());
        }

        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);


        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }


}




