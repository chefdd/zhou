package com.zhou.wenda.asynchronous.handle;

import com.zhou.wenda.asynchronous.EventHandler;
import com.zhou.wenda.asynchronous.EventModel;
import com.zhou.wenda.asynchronous.EventType;
import com.zhou.wenda.domain.Message;
import com.zhou.wenda.domain.User;
import com.zhou.wenda.service.MessageService;
import com.zhou.wenda.service.UserService;
import com.zhou.wenda.utils.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class CommentHandler implements EventHandler {

    @Resource
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Override
    public void doHandle(EventModel eventModel) {
        Message message = new Message();
        message.setCreatedDate(new Date());

        message.setFromId(WendaUtil.SYSTEM_USERID);
        message.setToId(eventModel.getEntityOwnerId());

        User user = userService.getUserById(eventModel.getActorId());
        message.setContent("用户：" + user.getName() + "评论了你的问题， http://127.0.0.1:8080/question/" + eventModel.getExts().get("questionId"));


        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.COMMENT);
    }
}
