package com.sss;

import com.sss.dao.NewsDAO;
import com.sss.dao.UserDAO;
import com.sss.model.News;
import com.sss.model.User;
import com.sun.xml.internal.bind.v2.TODO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

/**
 * Created by Shusheng Shi on 2017/5/7-2
 */
//数据库初始化测试用例

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("/init-schema.sql")
public class InitDatabaseTests {

    @Autowired
    public UserDAO userDAO;

    @Autowired
    public NewsDAO newsDAO;

    @Test
    public void contextLoads() {
        Random random = new Random();
        for (int i = 0; i < 11; i++) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("");
            user.setSalt("");
            userDAO.addUser(user);


            News news = new News();
            news.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
            news.setLikeCount(i + 1);
            news.setUserId(i + 1);
            news.setTitle(String.format("TITLE{%d}", i));
            news.setLink(String.format("http://wwww.nowcoder.com/%d.html", i));
            newsDAO.addNews(news);

            user.setPassword("newPassword");
            userDAO.updatePassword(user);
        }

        Assert.assertEquals("newPassword", userDAO.selectById(1).getPassword());
        userDAO.deleteById(1);
        Assert.assertNull(userDAO.selectById(1));
    }
}
