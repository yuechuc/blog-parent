package com.sangeng.mapper;

import com.sangeng.domain.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author yuechu
* @description 针对表【sg_article(文章表)】的数据库操作Mapper
* @createDate 2023-04-29 23:25:47
* @Entity com.sangeng.domain.Article
*/
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

}




