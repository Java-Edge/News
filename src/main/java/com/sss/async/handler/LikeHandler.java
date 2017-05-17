package com.sss.async.handler;

import com.sss.async.EventHandler;
import com.sss.async.EventModel;
import com.sss.async.EventType;
import com.sss.model.Message;
import com.sss.model.User;
import com.sss.service.MessageService;
import com.sss.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.resources.Messages;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Shusheng Shi on 2017/5/17 21:05.
 */
@Component
public class LikeHandler implements EventHandler {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(3);
        //点赞人
        User user = userService.getUser(model.getActorId());
        message.setContent("用户" + user.getName() + "赞了你的资讯,http:localhost:8080/news/" + model.getEntityId());
        message.setCreatedDate(new Date());
        messageService.addMessage(message);

    }

    /**
     *
     * @return 支持点赞事件
     */
    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }

}
