package com.sangeng.controller;

import com.sangeng.response.ResponseResult;
import com.sangeng.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/hotArticleList")
    public ResponseResult getHotArticleList(){
        ResponseResult result= articleService.getHotArticleList();
        return result;
    }
    //http://localhost:7777/article/articleList?pageNum=1&pageSize=10&categoryId=1
    @GetMapping("/articleList")
    public ResponseResult getArticleList(Integer pageNum,Integer pageSize,Long categoryId){

        return  articleService.getArticleList(pageNum,pageSize,categoryId);
    }

}