package com.abaka.chat_room.client.dao;

import com.abaka.chat_room.client.entity.User;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountDaoTest {
    AccountDao accountDao = new AccountDao();
    @Test
    public void register() {
        User user = new User();
        user.setUsername("阿巴卡1");
        user.setPassword("123456");
        user.setBrief("哈哈哈哈");
        boolean flag = accountDao.register(user);
        Assert.assertTrue(flag);
    }

    @Test
    public void login() {
        User user = new User();
        String username = "阿巴卡";
        String password = "12345";
        user = accountDao.login(username,password);
        Assert.assertNotNull(user);
        System.out.println(user);
    }
}