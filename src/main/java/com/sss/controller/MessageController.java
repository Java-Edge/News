package com.sss.controller;

/**
 * Created by Shusheng Shi on 2017/5/13 18:51.
 */

import com.sss.model.HostHolder;
import com.sss.model.Message;
import com.sss.model.User;
import com.sss.model.ViewObject;
import com.sss.service.MessageService;
import com.sss.service.UserService;
import com.sss.util.ToutiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 一个业务就得专门写一个控制器类
 */
@Controller
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private HostHolder hostHolder;

    /**
     * 获取信息详情
     * @param conversationId 会话的ID
     * @param model 模型
     *
     */
    @RequestMapping(path = "/msg/detail", method = RequestMethod.GET)
    public String conversationDetail(Model model, @Param("conversationId") String conversationId) {
        try {
            List<Message> conversationList = messageService.getConversationDetail(conversationId, 0, 10);
//          同时需要发消息用户的头像
            List<ViewObject> messages = new ArrayList<>();
            for (Message message : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("message", message);
//              发送人(为将其头像取出)
                User user = userService.getUser(message.getFromId());
                if (user == null) {
                    continue;
                }
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userId", user.getId());
                messages.add(vo);
            }
            model.addAttribute("messages", messages);
        } catch (Exception e) {
            logger.error("获取信息详情失败" + e.getMessage());
        }
        return "letterDetail.html";
    }

    /**
     * 添加评论
     * @param fromId
     * @param toId
     * @param content
     * @return
     */
    @RequestMapping(path = "/msg/addMessage", method = RequestMethod.POST)
    //因为是JSON串,所以
    @ResponseBody
    public String addComment(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content) {
        try {
            Message message = new Message();
            message.setFromId(fromId);
            message.setToId(toId);
            message.setCreatedDate(new Date());
            message.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
            message.setContent(content);
            messageService.addMessage(message);
            return ToutiaoUtil.getJSONString(message.getId());

        } catch (Exception e) {
            logger.error("增加评论失败", e.getMessage());
        }
        return null;
    }

    /**
     * 获取站内信列表
     * @param model
     * @return
     */
    @RequestMapping(path = "/msg/list", method = RequestMethod.GET)
    public String conversationDetail(Model model) {
        try {

            int localUserId = hostHolder.getUser().getId();
            List<ViewObject> conversations = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);
            for (Message message : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("conversation", message);
//              查出消息源头发出人
                int targetId = message.getFromId() == localUserId ? message.getToId() : message.getFromId();
//              获取此用户相关信息
                User user = userService.getUser(targetId);
                vo.set("target", user);
//              未读消息数
                vo.set("unread", messageService.getConvesationUnreadCount(localUserId, message.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations", conversations);
        } catch (Exception e) {
            logger.error("获取站内信列表失败" + e.getMessage());
        }
        return "letter";
    }

}
