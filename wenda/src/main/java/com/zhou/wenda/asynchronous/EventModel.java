package com.zhou.wenda.asynchronous;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class EventModel {

    private EventType type;     //事件
    private int actorId;        //触发者id（相当于fromId）

    //type + id 确定一个实体
    private int entityId;       //实体id
    private int entityType;     //实体type




    private int entityOwnerId;  //实体拥有者（相当于toId）

    private Map<String, String> exts = new HashMap<>(); //扩展字段，保存其他字段信息


    public EventModel() {
    }

    public EventModel(EventType type) {
        this.type = type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }


    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }


    public EventModel setExt(String key, String value){
        exts.put(key, value);
        return this;
    }
}
