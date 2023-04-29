package com.sangeng.controller;

import com.sangeng.response.ResponseResult;
import com.sangeng.service.ArticleService;
import com.sangeng.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/category")
public class  CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getCategoryList")
    public ResponseResult getCategoryList(){
        ResponseResult result= categoryService.getCategoryList();
        return result;
    }
}