package com.zhou.wenda.service;

import com.zhou.wenda.config.RedisKey;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FollowService {

    @Resource
    private RedJedis redJedis;

    /**
     * 用户关注某个实体，
     * A关注B：
     *      * 1、A的关注列表中加入B
     *      * 2、B的粉丝列表中加入A
     * @param entityIdA
     * @param entityIdB
     * @param entityType
     * @return
     */
    public boolean follow(int entityIdA, int entityIdB, int entityType) {
        String followerKey = RedisKey.getFollowerKey(entityType, entityIdB);
        String followeeKey = RedisKey.getFolloweeKey(entityIdA, entityType);

        Date date = new Date();
        Jedis jedis = redJedis.getJedis();
        Transaction tran = redJedis.multi(jedis);
        // 在实体的粉丝中增加当前用户
        tran.zadd(followerKey, date.getTime(), String.valueOf(entityIdA));
        // 在当前用户的关注对象中增加该实体
        tran.zadd(followeeKey, date.getTime(), String.valueOf(entityIdB));

        List<Object> ret = redJedis.exec(tran, jedis);
        return ret.size() == 2 && (Long)ret.get(0) > 0;
    }


    /**
     * 取消关注
     * @param userId
     * @param entityIdB
     * @param entityType
     * @return
     */
    public boolean unfollow(int userId, int entityIdB, int entityType) {
        String followerKey = RedisKey.getFollowerKey(entityType, entityIdB);
        String followeeKey = RedisKey.getFolloweeKey(userId, entityType);

        Jedis jedis = redJedis.getJedis();
        Transaction tran = redJedis.multi(jedis);
        // 在实体的粉丝中删除当前用户
        tran.zrem(followerKey, String.valueOf(userId));

        // 在当前用户的关注对象中删除该实体
        tran.zrem(followeeKey, String.valueOf(entityIdB));
        List<Object> ret = redJedis.exec(tran, jedis);
        return ret.size() == 2 && (Long)ret.get(0) > 0;
    }

    /**
     * 获取实体所有粉丝列表
     * @param entityType
     * @param entityId
     * @param count
     * @return
     */
    public List<Integer> getFollows(int entityType, int entityId, int count) {
        String followerKey = RedisKey.getFollowerKey(entityType, entityId);
        return redJedis.zrange(followerKey, 0, count)
                .stream().map(string->Integer.valueOf(string)).collect(Collectors.toList());
    }
    public List<Integer> getFollows(int entityType, int entityId, int offset, int count) {
        String followerKey = RedisKey.getFollowerKey(entityType, entityId);
        return redJedis.zrange(followerKey, offset, count)
                .stream().map(string->Integer.valueOf(string)).collect(Collectors.toList());
    }


    /**
     * 获取用户的所有关注对象
     * @param userId
     * @param entityType
     * @param count
     * @return
     */
    public List<Integer> getFollowees(int userId, int entityType, int count) {
        String followeeKey = RedisKey.getFolloweeKey(userId, entityType);
        return redJedis.zrange(followeeKey, 0, count)
                .stream().map(string-> Integer.valueOf(string)).collect(Collectors.toList());
    }

    public List<Integer> getFollowees(int userId, int entityType, int offset, int count) {
        String followeeKey = RedisKey.getFolloweeKey(userId, entityType);
        return redJedis.zrange(followeeKey, offset, count)
                .stream().map(string->Integer.valueOf(string)).collect(Collectors.toList());
    }

    /**
     * 获取实体的所有粉丝数量
     * @param entityType
     * @param entityId
     * @return
     */
    public long getFollowerCount(int entityType, int entityId) {
        String followerKey = RedisKey.getFollowerKey(entityType, entityId);
        return redJedis.zcard(followerKey);
    }

    /**
     * 获取用户所关注的数量
     * @param userId
     * @param entityType
     * @return
     */
    public long getFolloweeCount(int userId, int entityType) {
        String followeeKey = RedisKey.getFolloweeKey(userId, entityType);
        return redJedis.zcard(followeeKey);
    }

    /**
     * 判断用户是否关注了某个实体
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean isFollower(int userId, int entityType, int entityId) {
        String followerKey = RedisKey.getFollowerKey(entityType, entityId);
        return redJedis.zscore(followerKey, String.valueOf(userId)) != null;
    }

}
