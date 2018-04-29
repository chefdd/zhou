package com.zhou.wenda.asynchronous;

import java.util.List;

public interface EventHandler {


    void doHandle(EventModel eventModel); //处理事件

    List<EventType> getSupportEventTypes();     //注册所关注的事件


}
