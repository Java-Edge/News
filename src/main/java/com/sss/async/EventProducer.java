package com.sss.async;

import com.alibaba.fastjson.JSONObject;
import com.sss.util.JedisAdapter;
import com.sss.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Shusheng Shi on 2017/5/17 21:08.
 */

/**
 * 将事件的数据发出(放入队列中)
 */
@Service
public class EventProducer {
    @Autowired
    private JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel model) {
        try {
            //先将事件序列化,再将其放入队列中
            String json = JSONObject.toJSONString(model);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
