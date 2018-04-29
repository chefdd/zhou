package com.zhou.wenda.asynchronous.handle;

import com.alibaba.fastjson.JSONObject;
import com.zhou.wenda.asynchronous.EventHandler;
import com.zhou.wenda.asynchronous.EventModel;
import com.zhou.wenda.asynchronous.EventType;
import com.zhou.wenda.config.RedisKey;
import com.zhou.wenda.domain.EntityType;
import com.zhou.wenda.domain.Feed;
import com.zhou.wenda.domain.Question;
import com.zhou.wenda.domain.User;
import com.zhou.wenda.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Description: 新鲜事事件处理器:发生评论问题或关注问题事件时，异步地将新鲜事添加到数据库中
 */

@Component
public class FeedHandler implements EventHandler {

    @Resource
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private FeedService feedService;

    @Autowired
    private FollowService followService;

    @Autowired
    private RedJedis jedisAdapter;

    // 构造核心数据
    private String buildFeedData(EventModel model){
        Map<String, String> map = new HashMap<>();
        User actor = userService.getUserById(model.getActorId());
        if (actor == null){
            return null;
        }
        map.put("userId", String.valueOf(actor.getId()));
        map.put("userHead", actor.getHeadUrl());
        map.put("userName", actor.getName());

        if (model.getType() == EventType.COMMENT ||(model.getType() == EventType.FOLLOW && model.getEntityType() == EntityType.ENTITY_QUESTION)){
            Question question = questionService.selectById(model.getEntityId());
            if (question == null){
                return null;
            }
            map.put("questionId", String.valueOf(question.getId()));
            map.put("questionTitle", question.getTitle());
            return JSONObject.toJSONString(map);
        }
        return null;
    }

    @Override
    public void doHandle(EventModel model) {
        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setUserId(model.getActorId());
        feed.setType(model.getType().getValue());
        feed.setData(buildFeedData(model));
        if (feed.getData() == null){
            return;
        }

        feedService.addFeed(feed);

        // 给事件的粉丝推
        List<Integer> followers = followService.getFollows(EntityType.ENTITY_USER, model.getActorId(), Integer.MAX_VALUE);
        for (int follower : followers){
            String timelineKey = RedisKey.getTimelineKey(follower);
            jedisAdapter.lpush(timelineKey, String.valueOf(feed.getId()));
        }

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.COMMENT, EventType.FOLLOW});
    }
}
