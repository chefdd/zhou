package com.zhou.wenda.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Message {


    //私信
    private int id;
    private int fromId;
    private int toId;
    private String content;
    private Date createdDate;
    private int hasRead;
    private String conversationId;

    public Message(){}

    public Message(int id, int fromId, int toId, String content, Date createdDate, int hasRead, String conversationId) {
        this.id = id;
        this.fromId = fromId;
        this.toId = toId;
        this.content = content;
        this.createdDate = createdDate;
        this.hasRead = hasRead;
        this.conversationId = conversationId;
    }

    public String getConversationId() {
        if (fromId < toId){
            return String.format("%d_%d", fromId, toId);
        }else {
            return String.format("%d_%d", toId, fromId);
        }
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

}
