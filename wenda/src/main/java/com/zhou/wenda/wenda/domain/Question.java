package com.zhou.wenda.wenda.domain;


import lombok.Data;

import java.util.Date;

@Data
public class Question {

    private int id;
    private String title;
    private String content;
    private int userId;
    private Date createdDate;
    private int commentCount;

    public Question() {
    }

    public Question(int id, String title, String content, int userId, Date createdDate, int commentCount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.createdDate = createdDate;
        this.commentCount = commentCount;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", userId=" + userId +
                ", createdDate=" + createdDate +
                ", commentCount=" + commentCount +
                '}';
    }
}
