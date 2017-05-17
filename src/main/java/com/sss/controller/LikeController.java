package com.sss.controller;

/**
 * Created by Shusheng Shi on 2017/5/16 19:23.
 */

import com.sss.async.EventModel;
import com.sss.async.EventProducer;
import com.sss.async.EventType;
import com.sss.model.EntityType;
import com.sss.model.HostHolder;
import com.sss.service.LikeService;
import com.sss.service.NewsService;
import com.sss.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 前端的入口都在controller层中
 */
@Controller
public class LikeController {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private EventProducer eventProducer;

    /**
     * @param model
     * @param newsId 所喜欢资讯的ID
     * @return 将新的likeCount返回给前端, 则其可直接显示新的数量
     */
    @RequestMapping(path = "/like", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String like(Model model, @RequestParam("newsId") int newsId) {
        int userId = hostHolder.getUser().getId();
        long likeCount = likeService.like(userId, newsId, EntityType.ENTITY_NEWS);
//      因为News model类中有likeCount字段,所以也需要更新
        newsService.updateLikeCount(newsId, (int) likeCount);

        /**
         * 将事件当时现场数据记录下来
         * 点赞事件,谁点的,点的对象是哪个点的对象的拥有者是谁
         * 接着被消费队列取出
         */
        eventProducer.fireEvent(new EventModel(EventType.LIKE).
                setActorId(hostHolder.getUser().getId()).
                setEntityId(newsId).
                setEntityType(EntityType.ENTITY_NEWS).
                setEntityOwnerId(userId));

        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }

    /**
     * @param model
     * @param newsId 所喜欢资讯的ID
     * @return 将新的likeCount返回给前端, 则其可直接显示新的数量
     */
    @RequestMapping(path = "/dislike", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String dislike(Model model, @RequestParam("newsId") int newsId) {
        int userId = hostHolder.getUser().getId();
        long likeCount = likeService.disLike(userId, newsId, EntityType.ENTITY_NEWS);
//      因为News model类中有likeCount字段,所以也需要更新
        newsService.updateLikeCount(newsId, (int) likeCount);

        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }
}
