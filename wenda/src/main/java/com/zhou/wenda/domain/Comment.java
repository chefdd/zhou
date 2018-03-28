package com.zhou.wenda.domain;


import lombok.Data;

import java.util.Date;

@Data
public class Comment {


    //pinglun

    private int id;
    private String content;
    private int userId;
    private int entityId;
    private int entityType;
    private Date createdDate;
    private int status;//根据status状态来判断评论是否删除

    public Comment(){}

    public Comment(int id, String content, int userId, int entityId, int entityType, Date createdDate, int status) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.entityId = entityId;
        this.entityType = entityType;
        this.createdDate = createdDate;
        this.status = status;

    }

}
