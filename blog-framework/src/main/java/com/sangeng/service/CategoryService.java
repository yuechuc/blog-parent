package com.sangeng.service;

import com.sangeng.domain.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.dto.CategoryQueryDto;
import com.sangeng.domain.dto.TagDto;
import com.sangeng.response.ResponseResult;

/**
* @author yuechu
* @description 针对表【sg_category(分类表)】的数据库操作Service
* @createDate 2023-04-29 23:27:42
*/
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult listAllCategory();

    ResponseResult list(Integer pageNum, Integer pageSize, CategoryQueryDto categoryQueryDto);
}
