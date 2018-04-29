package com.zhou.wenda.asynchronous.handle;

import com.zhou.wenda.asynchronous.EventHandler;
import com.zhou.wenda.asynchronous.EventModel;
import com.zhou.wenda.asynchronous.EventType;
import com.zhou.wenda.config.RedisKey;
import com.zhou.wenda.domain.Message;
import com.zhou.wenda.domain.User;
import com.zhou.wenda.service.MessageService;
import com.zhou.wenda.service.RedJedis;
import com.zhou.wenda.service.UserService;
import com.zhou.wenda.utils.WendaUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Component
public class LikeHander implements EventHandler {



    @Resource
    private UserService userService;

    @Resource
    private MessageService messageService;

    public LikeHander() {
    }

    @Override
    public void doHandle(EventModel eventModel) {
        Message message = new Message();
        message.setToId(eventModel.getEntityOwnerId());
       // message.setFromId(WendaUtil.SYSTEM_USERID);
        message.setFromId(eventModel.getActorId());

        message.setCreatedDate(new Date());

        User user = userService.getUserById(eventModel.getActorId());

        message.setContent("用户：" + user.getName() + "赞了你的评论， http://127.0.0.1:8080/question/" + eventModel.getExts().get("questionId"));
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
