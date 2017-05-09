package com.sss.controller;

import com.sss.aspect.LogAspect;
import com.sss.model.News;
import com.sss.model.ViewObject;
import com.sss.service.NewsService;
import com.sss.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shusheng Shi on 2017/5/8.-1
 */
//首页功能
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/reg/", method = {RequestMethod.GET, RequestMethod.POST})
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember", defaultValue = "0") int rememberme) {

    }



}
