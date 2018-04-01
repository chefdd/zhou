package com.zhou.wenda.service;


import com.zhou.wenda.config.RedisKey;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LikeService {

    @Resource
    private RedJedis redJedis;

    /**
     * 获取点赞数
     * @param entityType
     * @param entityId
     * @return
     */
    public long getLikeCount(int entityType, int entityId) {
        String likeKey = RedisKey.getLikeKey(entityType, entityId);
        return redJedis.scard(likeKey);
    }

    /**
     * 获取当前用户的点赞状态 1 -1 0
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public int getLikeStatus(int userId, int entityType, int entityId) {
        String likeKey = RedisKey.getLikeKey(entityType, entityId);
        String disLikeKey = RedisKey.getDisLikeKey(entityType, entityId);

        if (redJedis.sismember(likeKey, String.valueOf(userId))){
            return 1;
        }
        if (redJedis.sismember(disLikeKey, String.valueOf(userId))) {
            return -1;
        }
        return 0;

    }

    /**
     * 赞
     * @param userId
     * @param type
     * @param entityId
     * @return
     */
    public long likeCount(int userId, int type, int entityId) {
        String likeKey = RedisKey.getLikeKey(type, entityId);
        String disLikeKey = RedisKey.getDisLikeKey(type, entityId);

        redJedis.sadd(likeKey, String.valueOf(userId));

        //Redis Srem 命令用于移除集合中的一个或多个成员元素，不存在的成员元素会被忽略。
        redJedis.srem(disLikeKey, String.valueOf(userId));
        return redJedis.scard(likeKey);
    }

    /**
     * 踩
     * @param userId
     * @param type
     * @param entityId
     * @return
     */
    public long dislikeCount(int userId, int type, int entityId) {
        String disLikeKey = RedisKey.getDisLikeKey(type, entityId);

        //Redis Srem 命令用于移除集合中的一个或多个成员元素，不存在的成员元素会被忽略。
        String likeKey = RedisKey.getLikeKey(type, entityId);
        redJedis.sadd(disLikeKey, String.valueOf(userId));

        redJedis.srem(likeKey, String.valueOf(userId));
        return redJedis.scard(likeKey);
    }




}
