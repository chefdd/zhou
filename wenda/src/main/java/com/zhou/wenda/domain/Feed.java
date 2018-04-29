package com.zhou.wenda.domain;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Date;

/**
 * 新鲜事
 */
@Data
public class Feed {

    private int id;
    private int type;
    private int userId;
    private Date createdDate;
    private String data;
    private JSONObject dataJSON = null;

    public Feed() {
    }

    public Feed(int id, int type, int userId, Date createdDate, String data) {
        this.id = id;
        this.type = type;
        this.userId = userId;
        this.createdDate = createdDate;
        this.data = data;
    }
}
