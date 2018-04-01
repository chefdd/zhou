package com.zhou.wenda.service;


import com.zhou.wenda.Dao.MessageDao;
import com.zhou.wenda.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private SensitiveService sensitiveService;

    public int addMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageDao.insertMessage(message) == true ? message.getId() : 0;
    }

    public List<Message> getConversationsDetailById(String conversationId){
        return messageDao.getConversationsDetailById(conversationId);

    }


    public List<Message> getConversationList(int userId) {
        return messageDao.getConversationList(userId);
    }
}
