package com.zhou.wenda.asynchronous;


import com.alibaba.fastjson.JSONObject;
import com.zhou.wenda.config.RedisKey;
import com.zhou.wenda.service.RedJedis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
@Slf4j
@Component
public class EventProducer {

    @Resource
    private RedJedis redJedis;

    public boolean fireEvent(EventModel eventModel) {

        try {
            //序列化后放入redis队列
            String Json = JSONObject.toJSONString(eventModel);
            String redisKey = RedisKey.getEventQueueKey();
            redJedis.lpush(redisKey, Json);
            return true;
        } catch (Exception e) {
            log.error("序列化失败" + e.getMessage());
            return false;
        }

    }


}
