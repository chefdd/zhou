package com.zhou.wenda.asynchronous.handle;

import com.zhou.wenda.asynchronous.EventHandler;
import com.zhou.wenda.asynchronous.EventModel;
import com.zhou.wenda.asynchronous.EventType;
import com.zhou.wenda.domain.EntityType;
import com.zhou.wenda.domain.Message;
import com.zhou.wenda.domain.User;
import com.zhou.wenda.service.MessageService;
import com.zhou.wenda.service.UserService;
import com.zhou.wenda.utils.WendaUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Component
public class FollowHandler implements EventHandler {

    @Resource
    private MessageService messageService;

    @Resource
    private UserService userService;


    @Override
    public void doHandle(EventModel eventModel) {
        Message message = new Message();
        //系统账号
        message.setFromId(WendaUtil.SYSTEM_USERID);

        message.setToId(eventModel.getEntityOwnerId());
        message.setCreatedDate(new Date());

        User user = userService.getUserById(eventModel.getActorId());
        if (eventModel.getEntityType() == EntityType.ENTITY_USER) {
            message.setContent("用户: " + user.getName() + "关注了你, http://127.0.0.1:8080/user/" + eventModel.getActorId());

        } else if (eventModel.getEntityType() == EntityType.ENTITY_QUESTION) {
            message.setContent("用户: " + user.getName() + "关注了你的问题, http://127.0.0.1:8080/question/" + eventModel.getEntityId());

        }

        messageService.addMessage(message);


    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW);
    }
}
