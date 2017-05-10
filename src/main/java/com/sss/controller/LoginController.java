package com.sss.controller;

import com.sss.aspect.LogAspect;
import com.sss.model.News;
import com.sss.model.ViewObject;
import com.sss.service.NewsService;
import com.sss.service.UserService;
import com.sss.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shusheng Shi on 2017/5/8.-1
 */
//首页功能
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private UserService userService;

    //    此函数直接调用各种service即可

    /**
     * 注册功能
     * @param model:模型
     * @param username:用户名
     * @param password:密码
     * @param rememberme:记住密码
     * @return
     */
    @RequestMapping(path = "/reg/", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember", defaultValue = "0") int rememberme) {


        try {
            Map<String, Object> map = userService.register(username, password);
            if (map.isEmpty()) {
                return ToutiaoUtil.getJSONString(0, "注册成功");
            } else {
                return ToutiaoUtil.getJSONString(1, map);
            }

        } catch (Exception e) {
//          注册异常时,须记录在日志内
            logger.error("注册异常" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "注册异常");
        }

    }

    //    此函数直接调用各种service即可

    /**
     * 登录功能
     * @param model
     * @param username
     * @param password
     * @param rememberme
     * @return
     */
    @RequestMapping(path = "/login/", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember", defaultValue = "0") int rememberme) {


        try {
            Map<String, Object> map = userService.register(username, password);
            if (map.isEmpty()) {
                return ToutiaoUtil.getJSONString(0, "注册成功");
            } else {
                return ToutiaoUtil.getJSONString(1, map);
            }

        } catch (Exception e) {
//          注册异常时,须记录在日志内
            logger.error("注册异常" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "注册异常");
        }

    }


}
