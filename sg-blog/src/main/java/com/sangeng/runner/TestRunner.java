package com.sangeng.runner;

import com.sangeng.domain.Article;
import com.sangeng.mapper.ArticleMapper;
import com.sangeng.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class TestRunner implements CommandLineRunner {




    @Override
    public void run(String... args) throws Exception {
        System.out.println("初始化ing");
    }
}
