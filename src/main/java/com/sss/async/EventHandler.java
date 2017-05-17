package com.sss.async;

import java.util.List;

/**
 * Created by Shusheng Shi on 2017/5/17 20:48.
 */
public interface EventHandler {
    //处理事件
    void doHandle(EventModel model);

    //支持的事件类型
    List<EventType> getSupportEventTypes();
}
