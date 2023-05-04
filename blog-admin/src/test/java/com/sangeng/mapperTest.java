package com.sangeng;

import com.sangeng.mapper.MenuMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class mapperTest {
    @Autowired
    private MenuMapper menuMapper;

    @Test
    public void menu(){
        List<String> list = menuMapper.selectPermsByUserId(2L);
        System.out.println(list);
    }
}
