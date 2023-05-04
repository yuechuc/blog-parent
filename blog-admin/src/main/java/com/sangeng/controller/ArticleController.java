package com.sangeng.controller;

import com.sangeng.domain.dto.ArticleDto;
import com.sangeng.domain.vo.adminVo.AdminArticleDto;
import com.sangeng.response.ResponseResult;
import com.sangeng.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @PostMapping
    public ResponseResult addArticle(@RequestBody ArticleDto articleDto){
        return articleService.addArticle(articleDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getById(@PathVariable Long id){
        return articleService.selectById(id);
    }

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum,Integer pageSize,ArticleDto articleDto){
        return articleService.list(pageNum,pageSize,articleDto);
    }



    @PutMapping
    public ResponseResult updateArticle(@RequestBody AdminArticleDto adminArticleDto){
        return articleService.updateArticle(adminArticleDto);
    }
}
