package com.sangeng.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.Category;
import com.sangeng.domain.Link;
import com.sangeng.domain.dto.LinkQueryDto;
import com.sangeng.domain.vo.CategoryVo;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.mapper.LinkMapper;
import com.sangeng.response.ResponseResult;
import com.sangeng.service.LinkService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.domain.vo.LinkVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-04-30 18:59:12
 */


@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {
    @Override
    public ResponseResult getgetAllLink() {
        //查询所有审核通过的
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> links = list(queryWrapper);
        //转换成vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        //封装返回
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult list(Integer pageNum, Integer pageSize, LinkQueryDto linkQueryDto) {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(StringUtils.hasText(linkQueryDto.getName()),Link::getName,linkQueryDto.getName())
                .eq(StringUtils.hasText(linkQueryDto.getStatus()),Link::getStatus,linkQueryDto.getStatus())
                .orderByDesc(Link::getUpdateTime);

        Page<Link> page  = new Page<>(pageNum, pageSize);
        Page<Link> linkPage = this.page(page, queryWrapper);

        List<LinkVo> linkVos= BeanCopyUtils.copyBeanList(linkPage.getRecords(), LinkVo.class);
        PageVo pageVo = new PageVo(linkVos, linkPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult changeStatus(Long id, String status) {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getId,id);
        Link link = this.getOne(queryWrapper);
        link.setStatus(status);
        this.updateById(link);
       return ResponseResult.okResult();
    }
}