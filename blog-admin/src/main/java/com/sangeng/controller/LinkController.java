package com.sangeng.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.sangeng.domain.Category;
import com.sangeng.domain.Link;
import com.sangeng.domain.dto.*;
import com.sangeng.domain.vo.LinkVo;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.exception.SystemException;
import com.sangeng.response.ResponseResult;
import com.sangeng.service.LinkService;
import com.sangeng.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, LinkQueryDto linkQueryDto) {
        return linkService.list(pageNum, pageSize, linkQueryDto);
    }

    @Transactional
    @PostMapping
    public ResponseResult addCategory(@RequestBody LinkAddDto linkAddDto) {
        Link link = BeanCopyUtils.copyBean(linkAddDto, Link.class);
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Link::getName, linkAddDto.getName());
        List<Link> list = linkService.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            linkService.save(link);
            return ResponseResult.okResult();
        } else {
            //重名，数据还没设计唯一约束
            throw new SystemException(AppHttpCodeEnum.NAME_EXIST);
        }
    }

    @GetMapping("/{id}")
    public ResponseResult getById(@PathVariable Long id) {
        Link link = linkService.getById(id);
        LinkAddDto linkAddDto = BeanCopyUtils.copyBean(link, LinkAddDto.class);
        return ResponseResult.okResult(linkAddDto);
    }


    @PutMapping
    public ResponseResult updateLink(@RequestBody LinkVo linkVo) {
        Link link = BeanCopyUtils.copyBean(linkVo, Link.class);
        linkService.updateById(link);
        return ResponseResult.okResult();

    }

    @Transactional
    @DeleteMapping("/{ids}")
    public ResponseResult deleteLinkById(@PathVariable List<Long> ids){
        if (linkService.removeByIds(ids)){
            return ResponseResult.okResult();
        }else {
            throw new SystemException(AppHttpCodeEnum.OPERATION_ERROR);
        }
    }

    @PutMapping("/changeLinkStatus")
    public ResponseResult changeStatus(@RequestBody LinkStatusDto linkStatusDto) {
        return linkService.changeStatus(linkStatusDto.getId(), linkStatusDto.getStatus());
    }
}
