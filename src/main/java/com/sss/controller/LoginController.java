package com.sss.controller;

import com.sss.aspect.LogAspect;
import com.sss.service.UserService;
import com.sss.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Shusheng Shi on 2017/5/8.-1
 */
//首页功能
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    //    此函数直接调用各种service即可

    /**
     * 注册
     * @param model:模型
     * @param username:用户名
     * @param password:密码
     * @param rememberme:记住密码
     * @param response :使用response将这些数据写入cookie
     * @return
     */
    @RequestMapping(path = "/reg/", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember", defaultValue = "0") int rememberme,
                      HttpServletResponse response) {


        try {
            Map<String, Object> map = userService.register(username, password);
//          注册之后自动登录成功,被下发ticket
            if ((map.containsKey("ticket"))) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                response.addCookie(cookie);
//              设置此cookie全站有效
                cookie.setPath("/");
//              若用户选择记住密码,则将cookie有效期延长到五天,否则默认浏览器关闭就失效
                if (rememberme > 0) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }

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
     * 登录
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
//          若登录成功,则会被下发ticket
            if (map.containsKey("ticket")) {
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

    /**
     * 登出
     * @param ticket :token值
     * @return :重定向到首页
     */
    @RequestMapping(path = "/logout/", method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }

}
