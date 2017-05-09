package com.sss.controller;

import com.sss.aspect.LogAspect;
import com.sss.model.User;
import com.sss.service.ToutiaoService;
import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by Shusheng Shi on 2017/5/6.
 */
@Controller
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private ToutiaoService toutiaoService;

    @RequestMapping("/index")
    @ResponseBody
    public String index(HttpSession httpSession) {
        toutiaoService.say();
        logger.info("Visit Index");
        return "Hello Nowcoder" + httpSession.getAttribute("msg")+"<br>";

    }


    @RequestMapping("/request")
    @ResponseBody
    public String request(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            sb.append(name + ":" + request.getHeader(name) + "<br>");
        }
        for (Cookie cookie : request.getCookies()) {
            sb.append("Cookie:");
            sb.append(cookie.getName());
            sb.append(":");
            sb.append(cookie.getValue());
            sb.append("<br>");
        }

        sb.append("getMethod: " + request.getMethod() + "<br>");
        sb.append("getPathInfo: " + request.getPathInfo() + "<br>");
        sb.append("getQueryString: " + request.getQueryString() + "<br>");
        sb.append("getRequestURI: " + request.getRequestURI() + "<br>");



        return sb.toString();
    }

    @RequestMapping("/response")
    @ResponseBody
    public String response(@CookieValue(value = "nowcoderId", defaultValue = "a") String nowcoderId,
                           @RequestParam(value = "key", defaultValue = "key") String key,
                           @RequestParam(value = "value", defaultValue = "value") String value,
                           HttpServletResponse response) {
        response.addCookie(new Cookie(key, value));
        response.addHeader(key, value);
        return "NowcoderId from" + nowcoderId;
    }

    @RequestMapping("/redirect/{code}")
    @ResponseBody
    public String  redirect(@PathVariable("code") int code,HttpSession session) {
       /* RedirectView redirectView = new RedirectView("/",true);
        if (code == 301) {
            redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }*/
        session.setAttribute("msg", "jump from redirect");
        return "redirect:/";
    }

    @ExceptionHandler
    @ResponseBody
    public String error(Exception e) {
        return "error:" + e.getMessage();
    }

}
