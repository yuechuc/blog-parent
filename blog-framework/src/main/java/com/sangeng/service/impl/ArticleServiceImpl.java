package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.RedisKey;
import com.sangeng.domain.Article;
import com.sangeng.domain.ArticleTag;
import com.sangeng.domain.Category;
import com.sangeng.domain.dto.ArticleDto;
import com.sangeng.domain.vo.*;
import com.sangeng.domain.vo.adminVo.AdminArticleDto;
import com.sangeng.domain.vo.adminVo.AdminArticleListVo;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.exception.SystemException;
import com.sangeng.response.ResponseResult;
import com.sangeng.service.ArticleService;
import com.sangeng.mapper.ArticleMapper;
import com.sangeng.service.ArticleTagService;
import com.sangeng.service.CategoryService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RedisCache redisCache;
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

        for (Article article : articles) {
            Long id = article.getId();
            //从redis中获取viewCount
            Integer viewCount = redisCache.getCacheMapValue(RedisKey.ARTICLE_VIEWCOUNT, id.toString());
            article.setViewCount(viewCount.longValue());
        }
        //bean拷贝
        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);

        return ResponseResult.okResult(hotArticleVos);
    }

    @Override
    public ResponseResult getArticleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        Page<Article> articlePage = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 如果 有categoryId 就要 查询时要和传入的相同
        queryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0, Article::getCategoryId, categoryId);
        queryWrapper.eq(Article::getStatus, ARTICLE_STATUS_NORMAL);
        queryWrapper.orderByDesc(Article::getIsTop).orderByDesc(Article::getUpdateTime);
        IPage<Article> page = this.page(articlePage, queryWrapper);
        //查询categoryName
        List<Article> articles = page.getRecords();
        for (Article article : articles) {
            Category category = categoryService.getById(article.getCategoryId());
            article.setCategoryName(category.getName());
        }
        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);


        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticledetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue(RedisKey.ARTICLE_VIEWCOUNT, id.toString());
        article.setViewCount(viewCount.longValue());
        //转换成VO
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if(category!=null){
            articleDetailVo.setCategoryName(category.getName());
        }
        //封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中对应 id的浏览量
        redisCache.incrementCacheMapValue(RedisKey.ARTICLE_VIEWCOUNT,id.toString(),1);
        return ResponseResult.okResult();
    }

    @Transactional
    @Override
    public ResponseResult addArticle(ArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        this.save(article);

        List<String> tags = articleDto.getTags();
        for (String tag : tags) {
            ArticleTag articleTag = new ArticleTag();
            articleTag.setArticleId(article.getId());
            articleTag.setTagId(tag);
            articleTagService.save(articleTag);
        }

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult list(Integer pageNum, Integer pageSize, ArticleDto articleDto) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .like(StringUtils.hasText(articleDto.getTitle()),Article::getTitle,articleDto.getTitle())
                .like(StringUtils.hasText(articleDto.getSummary()),Article::getSummary,articleDto.getSummary())
                .orderByDesc(Article::getUpdateTime);
        Page<Article> page = new Page<>(pageNum, pageSize);

        Page<Article> articlePage = articleService.page(page, queryWrapper);

        List<AdminArticleListVo> adminArticleListVos = BeanCopyUtils.copyBeanList(articlePage.getRecords(), AdminArticleListVo.class);
        PageVo pageVo = new PageVo(adminArticleListVos, articlePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult selectById(Long id) {
        if (Objects.isNull(id)){
            throw new SystemException(AppHttpCodeEnum.OPERATION_ERROR);
        }
        Article article = articleService.getById(id);

        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,id);
        List<ArticleTag> articleTagList = articleTagService.list(queryWrapper);
        List<String> tagList = articleTagList.stream()
                .map(ArticleTag::getTagId)
                .collect(Collectors.toList());

        AdminArticleDto adminArticleDto = BeanCopyUtils.copyBean(article, AdminArticleDto.class);
        adminArticleDto.setTags(tagList);

        return ResponseResult.okResult(adminArticleDto);
    }

    @Transactional
    @Override
    public ResponseResult updateArticle(AdminArticleDto adminArticleDto) {
        List<String> tags = adminArticleDto.getTags();

        Article article = BeanCopyUtils.copyBean(adminArticleDto, Article.class);
        articleService.updateById(article);

        //先删除关联表
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,article.getId());
        articleTagService.remove(queryWrapper);

        for (String tag : tags) {
            ArticleTag articleTag = new ArticleTag(adminArticleDto.getId(), tag);
            //检查是否存在
            if (!existsArticleTag(articleTag)){
                articleTagService.save(articleTag);
            }
        }
        return ResponseResult.okResult();
    }


    public boolean existsArticleTag(ArticleTag articleTag) {
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTag::getArticleId, articleTag.getArticleId())
                .eq(ArticleTag::getTagId, articleTag.getTagId());
        return articleTagService.getOne(wrapper) != null;
    }


}




