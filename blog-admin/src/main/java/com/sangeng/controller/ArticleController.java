package com.sangeng.controller;

import com.sangeng.domain.dto.ArticleDto;
import com.sangeng.domain.vo.adminVo.AdminArticleDto;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.exception.SystemException;
import com.sangeng.response.ResponseResult;
import com.sangeng.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    //逻辑删除文章
    @DeleteMapping("/{ids}")
    public ResponseResult removeById(@PathVariable List<Long> ids){
        if (StringUtils.hasText(String.valueOf(ids))){
            articleService.removeByIds(ids);
            return ResponseResult.okResult();
        }else {
            throw new SystemException(AppHttpCodeEnum.OPERATION_ERROR);
        }

    }
}
