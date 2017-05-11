package com.sss.controller;

import com.sss.service.NewsService;
import com.sss.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;

/**
 * Created by Shusheng Shi on 2017/5/11 19:35.
 */
public class NewsController {

    @Autowired
    private NewsService newsService;

    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    @RequestMapping(path = "/uploadImage", method = RequestMethod.POST)
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
//          前端访问需要此url
            String fileUrl = newsService.saveImage(file);
            if (fileUrl == null) {
                return ToutiaoUtil.getJSONString(1, "上传图片失败");
            }

            return ToutiaoUtil.getJSONString(0,fileUrl);

        } catch (Exception e) {
            logger.error("上传图片失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "上传失败");
        }
    }


    @RequestMapping(path = "/getImage", method = RequestMethod.POST)
//  图片本是二进制流,不需要模板渲染因此用responsebody
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName, HttpServletResponse response) {
//        将图片返回给前端
        response.setContentType("image/jpeg");

        try {
            StreamUtils.copy(
                    new FileInputStream(ToutiaoUtil.IMAGE_DIR + imageName),
                    response.getOutputStream());
            //TODO:响应流相关知识复习
        } catch (Exception e) {
            logger.error("图片读取错误" + e.getMessage());
        }
    }
}
