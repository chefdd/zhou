package com.zhou.wenda.wenda.domain;

import lombok.Data;

import java.util.Date;

@Data
public class LoginTicket {

    private int id;
    private int userId;
    private String ticket;
    private Date expired;
    private int status;
    public LoginTicket() {
    }

    public LoginTicket(int id, int userId, String ticket, Date expired, int status) {
        this.id = id;
        this.userId = userId;
        this.ticket = ticket;
        this.expired = expired;
        this.status = status;
    }

}
