package com.sangeng.controller;

import com.sangeng.response.ResponseResult;
import com.sangeng.service.CategoryService;
import com.sangeng.service.CommentService;
import com.sangeng.service.LinkService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.sangeng.domian.Comment;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId, Integer pageNum,Integer pageSize){
        return  commentService.commentList(articleId,pageNum,pageSize);
    }
    @PostMapping
    public ResponseResult addComment(@RequestBody Comment comment){
        return  commentService.addComment(comment);
    }


}
