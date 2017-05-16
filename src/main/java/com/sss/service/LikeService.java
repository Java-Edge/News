package com.sss.service;

import com.sss.util.JedisAdapter;
import com.sss.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Shusheng Shi on 2017/5/16 17:05.
 */
@Service
public class LikeService {

    @Autowired
    private JedisAdapter jedisAdapter;

    /**
     * 某用户对于某元素是否喜欢状态
     * @param userId 用户ID
     * @param entityId 元素ID
     * @param entityType 元素类型
     * @return 喜欢:1,不喜欢:-1,其他:0
     */
    public int getLikeStatus(int userId,  int entityId,int entityType) {
//      所有喜欢此元素的集合的key
        String likeKey = RedisKeyUtil.getLikekey(entityId, entityType);
//      判断此用户是否在喜欢此元素的集合内
        if (jedisAdapter.sismember(likeKey, String.valueOf(userId))) {
            return 1;
        }
        String dislikeKey = RedisKeyUtil.getDisLikekey(entityId, entityType);
        return jedisAdapter.sismember(dislikeKey, String.valueOf(userId)) ? -1 : 0;
    }

    /**
     * 喜欢功能
     * @param userId 用户ID
     * @param entityId 元素ID
     * @param entityType 元素类型
     * @return 喜欢总人数
     */
    public long like(int userId, int entityId, int entityType) {
        String likeKey = RedisKeyUtil.getLikekey(entityId, entityType);
        jedisAdapter.sadd(likeKey, String.valueOf(userId));

//      从不喜欢集合中删除被选为喜欢的用户ID
        String dislikeKey = RedisKeyUtil.getDisLikekey(entityId, entityType);
        jedisAdapter.srem(dislikeKey, String.valueOf(userId));
        return jedisAdapter.scard(likeKey);

    }

    /**
     * 不喜欢功能
     * @param userId 用户ID
     * @param entityId 元素ID
     * @param entityType 元素类型
     * @return 不喜欢总人数
     */
    public long disLike(int userId, int entityId, int entityType) {
        String disLikeKey = RedisKeyUtil.getDisLikekey(entityId, entityType);
        jedisAdapter.sadd(disLikeKey, String.valueOf(userId));

//      从喜欢集合中删除被选为不喜欢的用户ID
        String likeKey = RedisKeyUtil.getLikekey(entityId, entityType);
        jedisAdapter.srem(likeKey, String.valueOf(userId));

//      可能多个线程在操作此变量,不能简单地对其加减,所以返回其
        return jedisAdapter.scard(likeKey);
    }
}
