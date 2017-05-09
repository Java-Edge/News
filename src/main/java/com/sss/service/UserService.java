package com.sss.service;

import com.sss.dao.UserDAO;
import com.sss.model.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shusheng Shi on 2017/5/7-4
 */
@Service
public class UserService {

//    由service层调用dao层,所以需要注入dao
    @Autowired
    private UserDAO userDAO;

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    //  添加注册用户
    public Map<String,Object> register(String username, String password) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(username)) {
            map.put("msgname", "用户名不能为空");
        }

        if (StringUtils.isBlank(password)) {
            map.put("msgpassword", password);
        }

        User user = userDAO.selectByName(username);
        if (user != null) {
            
        }


    }
}
