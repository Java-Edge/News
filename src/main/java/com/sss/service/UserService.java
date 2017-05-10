package com.sss.service;

import com.sss.dao.LoginTicketDAO;
import com.sss.dao.UserDAO;
import com.sss.model.LoginTicket;
import com.sss.model.User;
import com.sss.util.ToutiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Shusheng Shi on 2017/5/7-4
 */
@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    //  添加注册用户
    public Map<String,Object> register(String username, String password) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(username)) {
            map.put("msgname", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msgpassword", password);
            return map;
        }

        User user = userDAO.selectByName(username);
        if (user != null) {
            map.put("msgname", "此用户名已经被注册");
            return map;
        }

//      密码强度
        user = new User();
        user.setName(username);

        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        user.setPassword(ToutiaoUtil.MD5(password + user.getSalt()));
        //直接放入密码,则是明文保存,这是绝对禁止的!!!
//      user.setPassword(password);
        userDAO.addUser(user);
        return map;


    }

    /**
     * 登录
     * @param username:用户名
     * @param password:密码
     * @return
     */
    public Map<String,Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(username)) {
            map.put("msgname", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msgpassword", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if (user == null) {
            map.put("msgname", "用户名不存在");
            return map;
        }


//      判断用户存在后,开始验证密码
        if (!ToutiaoUtil.MD5(password + user.getSalt()).equals(user.getPassword())) {
            map.put("msgpwd", "密码不正确");
            return map;
        }

//      注册成功,就直接下发ticket,然后用户就直接登录了
//      用户登录成功
//      给用户下发ticket
        String ticket = addLoginTicket(user.getId());
//      将ticket保存下来,发到前端页面
        map.put("ticket", ticket);
        return map;
    }

    private String addLoginTicket(int userId) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
//      设置ticket有效期为一天
        Date date = new Date();
        date.setTime(date.getTime()+1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
//      将UUID中的"-"替换成""以确保为随机的字符串
        ticket.setTicket(UUID.randomUUID().toString().replace("-", ""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }
}
