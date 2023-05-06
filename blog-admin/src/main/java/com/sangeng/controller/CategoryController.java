package com.sangeng.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.sangeng.domain.Category;
import com.sangeng.domain.Tag;
import com.sangeng.domain.dto.AddCategoryDto;
import com.sangeng.domain.dto.CategoryQueryDto;
import com.sangeng.domain.dto.TagDto;
import com.sangeng.domain.vo.ExcelCategoryVo;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.exception.SystemException;
import com.sangeng.response.ResponseResult;
import com.sangeng.service.CategoryService;
import com.sangeng.service.TagService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory() {
        return categoryService.listAllCategory();
    }

    //导出分类Excel
    @PreAuthorize("@perms.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx", response);
            //获取需要导出的数据
            List<Category> categoryVos = categoryService.list();

            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel
                    .write(response.getOutputStream(), ExcelCategoryVo.class)
                    .autoCloseStream(Boolean.FALSE)
                    .sheet("分类导出")
                    .doWrite(excelCategoryVos);

        } catch (Exception e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, CategoryQueryDto categoryQueryDto) {
        return categoryService.list(pageNum, pageSize, categoryQueryDto);
    }

    @Transactional
    @PostMapping
    public ResponseResult addCategory(@RequestBody AddCategoryDto addCategoryDto) {
        Category category = BeanCopyUtils.copyBean(addCategoryDto, Category.class);
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getName, addCategoryDto.getName());
        List<Category> list = categoryService.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            categoryService.save(category);
            return ResponseResult.okResult();
        } else {
            //重名，数据还没设计唯一约束
            throw new SystemException(AppHttpCodeEnum.CATEGORY_NAME_EXIST);
        }
    }


    @Transactional
    @DeleteMapping("/{ids}")
    public ResponseResult deleteCategoryById(@PathVariable List<Long> ids){
        if (categoryService.removeByIds(ids)){
            return ResponseResult.okResult();
        }else {
            throw new SystemException(AppHttpCodeEnum.OPERATION_ERROR);
        }
    }
}
