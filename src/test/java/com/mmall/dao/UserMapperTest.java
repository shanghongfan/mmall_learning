package com.mmall.dao;

import com.mmall.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
    @ContextConfiguration(locations= "classpath:applicationContext-datasource.xml")
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;
    @Test
    public void selectLoginTest() {

        User user = userMapper.selectLogin("a", "a");

        System.out.println(user);


    }
}