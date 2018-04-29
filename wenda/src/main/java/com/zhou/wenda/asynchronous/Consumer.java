package com.zhou.wenda.asynchronous;

import com.alibaba.fastjson.JSONObject;
import com.zhou.wenda.config.RedisKey;
import com.zhou.wenda.service.RedJedis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 消费者
 */
@Service
@Slf4j
public class Consumer implements InitializingBean, ApplicationContextAware {

    @Resource
    private RedJedis redJedis;

    private Map<EventType, List<EventHandler>> config = new HashMap<>();
    private ApplicationContext applicationContext;


    @Override
    public void afterPropertiesSet() throws Exception {

        // 获取EventHandler接口所有的实现类
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null) {
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                for (EventType eventType : eventTypes) {
                    if (!config.containsKey(eventType)) {
                        config.put(eventType, new ArrayList<>());
                    }
                    config.get(eventType).add(entry.getValue());
                }
            }
        }

        //创建一个线程，一直从事件队列中取事件
        Thread thread = new Thread(new Runnable(){

            @Override
            public void run() {

                while (true) {
                    String key = RedisKey.getEventQueueKey();
                    List<String> eventList = redJedis.brpop(0, key);

                    for (String event : eventList) {
                        if (event.equals(key)) {
                            continue;
                        }

                        EventModel eventModel = JSONObject.parseObject(event, EventModel.class);

                        if (!config.containsKey(eventModel.getType())) {
                            log.error("事件未注册");
                        }

                        for (EventHandler handler : config.get(eventModel.getType())) {
                            handler.doHandle(eventModel);

                        }
                    }
                }
            }
        });
        thread.start();


    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public int reverse(int x) {
        long res = 0;
        while (x != 0){
            res = res * 10 + x % 10;
            x = x / 10;
        }
        if (x > Integer.MAX_VALUE){
            return 0;
        }

        return (int)res;

    }
}
