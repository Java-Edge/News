package com.sss.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Shusheng Shi on 2017/5/6.
 */
@Controller
public class SettingController {

    @RequestMapping("/setting")
    @ResponseBody
    public String setting() {
        return "Setting:OK";
    }
}
