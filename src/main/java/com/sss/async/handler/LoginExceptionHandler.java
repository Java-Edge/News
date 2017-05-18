package com.sss.async.handler;

/**
 * Created by Shusheng Shi on 2017/5/18 19:18.
 */

import com.sss.async.EventHandler;
import com.sss.async.EventModel;
import com.sss.async.EventType;
import com.sss.model.Message;
import com.sss.service.MessageService;
import com.sss.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 登录异常处理器
 */
@Component
public class LoginExceptionHandler implements EventHandler {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MailSender mailSender;


    @Override
    public void doHandle(EventModel model) {

        //判断是否有异常登录
        //发送站内信
        Message message = new Message();
        //谁登录了就发给谁
        message.setToId(model.getActorId());
        message.setContent("你上次的登录ip异常");
        message.setFromId(3);
        message.setCreatedDate(new Date());
        messageService.addMessage(message);

        Map<String, Object> map = new HashMap<>();
        map.put("username", model.getExt("username"));
        mailSender.sendWithHTMLTemplate(model.getExt("email"), "登录异常", "mails/welcome.html", map);
    }

    /**
     * 关注类型
     *
     * @return
     */
    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }

}
