package com.sangeng;

import com.sangeng.domain.LoginUser;
import com.sangeng.utils.SecurityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class uploadTest {


    @Test
    public  void test() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
    }


}
