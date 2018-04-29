package com.zhou.wenda.asynchronous;


public enum EventType {
    LIKE(0),        //点赞事件
    DISLIKE(1),     //点踩事件
    COMMENT(2),     //评论事件
    LOGIN(3),       //登录事件
    MAIL(4),        //邮件发送事件
    FOLLOW(5),      //关注事件
    UNFOLLOW(6),    //取消关注事件
    ADD_QUESTION(7);//增加问题

    private int value;

    EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
