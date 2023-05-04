package com.sangeng.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sangeng.domain.Tag;
import com.sangeng.domain.dto.TagDto;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.exception.SystemException;
import com.sangeng.response.ResponseResult;
import com.sangeng.service.TagService;
import com.sangeng.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/list")
        public ResponseResult list(Integer pageNum, Integer pageSize, TagDto tagDto){
        return tagService.pageTagList(pageNum,pageSize,tagDto);
    }
    @PostMapping
    public ResponseResult addTag(@RequestBody TagDto tagDto){
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
      if (tagService.save(tag)){
          return ResponseResult.okResult();
      }else {
          //重名，数据还没设计唯一约束
          throw new SystemException(AppHttpCodeEnum.OPERATION_ERROR);
      }
    }

    @Transactional
    @DeleteMapping("/{ids}")
    public ResponseResult deleteTagById(@PathVariable List<Long> ids){
        //tagService.removeByIds(ids);
        //return ResponseResult.okResult();
        if (tagService.removeByIds(ids)){
            return ResponseResult.okResult();
        }else {
            throw new SystemException(AppHttpCodeEnum.OPERATION_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseResult getTagInfo(@PathVariable Long id) {
        Tag tag = tagService.getById(id);
        return ResponseResult.okResult(tag);
    }

    @PutMapping
    public ResponseResult updateTagInfo(@RequestBody Tag tag) {
        if (tagService.updateById(tag)){
            return ResponseResult.okResult();
        }else {
            throw new SystemException(AppHttpCodeEnum.OPERATION_ERROR);
        }
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        return tagService.listAllTag();
    }

}


