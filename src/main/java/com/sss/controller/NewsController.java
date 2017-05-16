package com.sss.controller;

import com.sss.model.*;
import com.sss.service.*;
import com.sss.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.util.*;

/**
 * Created by Shusheng Shi on 2017/5/11 19:35.
 */
public class NewsController {

    @Autowired
    private UserService userService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private QiniuService qiniuService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

//  当前用户是否处于登录状态
    @Autowired
    private HostHolder hostHolder;

    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);


    @RequestMapping(path = "/uploadImage/", method = RequestMethod.POST)
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
//          前端访问需要此url
//          String fileUrl = newsService.saveImage(file);
            String fileUrl = qiniuService.saveImage(file);
            if (fileUrl == null) {
                return ToutiaoUtil.getJSONString(1, "上传图片失败");
            }

            return ToutiaoUtil.getJSONString(0,fileUrl);

        } catch (Exception e) {
            logger.error("上传图片失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "上传失败");
        }
    }


    @RequestMapping(path = "/getImage/", method = RequestMethod.POST)
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


    /**
     * 添加资讯
     * 将其入库
     * @param image :图片
     * @param title :标题
     * @param link :链接
     * @return 添加成功与否
     */
    @RequestMapping(path = "/user/addNews/", method = RequestMethod.POST)
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link) {
        try {
            News news = new News();
            if (hostHolder.getUser() != null) {
                news.setUserId(hostHolder.getUser().getId());
            } else {
                news.setImage(image);
                news.setTitle(title);
                news.setLink(link);
                newsService.addNews(news);
                return ToutiaoUtil.getJSONString(0);
            }

        } catch (Exception e) {
            logger.error("添加咨询错误", e.getMessage());
//          发布错误时会有前端的统一回复
            return ToutiaoUtil.getJSONString(1, "发布失败");
        }
        return null;
    }

    /**
     * 资讯详情
     * @param newsId 资讯ID
     * @param model 模型
     * @return 详情页面
     */
    @RequestMapping(path = "/news/{newsId}", method = RequestMethod.POST)
    @ResponseBody
    public String newsDetail(@PathVariable("newsId") int newsId, Model model) {
        News news = newsService.getById(newsId);

        if (news != null) {

            int localUserId = hostHolder.getUser().getId();
            if (localUserId != 0) {
                model.addAttribute("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
            } else {
                model.addAttribute("like", 0);
            }

//          评论
//          找出资讯关联的所有评论
            List<Comment> comments = commentService .getCommentsByEntity(news.getId(), EntityType.ENTITY_NEWS);

            //同时需要用户的头像
            List<ViewObject> commentVOs = new ArrayList<>();
            for (Comment comment : comments) {
                ViewObject vo = new ViewObject();
                vo.set("comment", comment);
                vo.set("user",userService.getUser(comment.getUserId()) );
                commentVOs.add(vo);
            }
            model.addAttribute("comments", commentVOs);
        }

        model.addAttribute("news", news);
//      资讯作者:通过咨询获取此用户ID进而获得此用户
        model.addAttribute("owner", userService.getUser(news.getUserId()));
        return "detail";
    }

    /**
     * 增加评论
     * @param newsId  所关联的资讯ID
     * @param content 评论内容
     */
    @RequestMapping(path = "/addComment", method = RequestMethod.POST)
    public String addComment(@RequestParam("newsId") int newsId,
                             @RequestParam("content") String content) {
        try {
//          过滤content
            Comment comment = new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            commentService.addComment(comment);

//          更新news里的评论数量
//          此处comment.getEntityId()其实就是参数newsId
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            newsService.updateCommentCount(comment.getEntityId(), count);
//          如何异步化




        } catch (Exception e) {
            logger.error("增加评论失败", e.getMessage());
        }
//      评论增加完毕后,刷新回到此资讯页面
        return "redirect:/news/" + String.valueOf(newsId);
    }

}
