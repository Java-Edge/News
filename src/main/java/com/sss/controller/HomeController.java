package com.sss.controller;

import com.sss.model.News;
import com.sss.model.ViewObject;
import com.sss.service.NewsService;
import com.sss.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shusheng Shi on 2017/5/8.-1
 */
//首页功能
@Controller
public class HomeController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private UserService userService;

    public List<ViewObject> getNews(int userId, int offset, int limit) {
        List<News> newsList = newsService.getLatestNews(userId, offset, limit);
//        页面需要显示用户头像,而头像在用户表中
        List<ViewObject> vos = new ArrayList<>();
        for (News news : newsList) {
            ViewObject vo = new ViewObject();
            vo.set("news", news);
//          通过新闻中的用户id获取userID
            vo.set("user", userService.getUser(news.getUserId()));
            vos.add(vo);
        }
        return vos;
    }

//    增加入口,将首页展示出来
    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model) {
        model.addAttribute("vos", getNews(0, 0, 10));
        return "home";
    }

//  通过user也能链接到首页
    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model) {
        model.addAttribute("vos", getNews(0, 0, 10));
        return "home";
    }
}
