package com.zhou.wenda.service;


import com.zhou.wenda.Dao.LoginTicketDao;
import com.zhou.wenda.domain.LoginTicket;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@Service
public class TicketService {

    @Resource
    private LoginTicketDao loginTicketDao;


    /**
     * 成功登录和注册，下发ticket
     * @param userId
     * @return
     */
    public String addTicket(int userId) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        loginTicket.setTicket(UUID.randomUUID().toString().replace("-", ""));
        Date date = new Date();
        date.setTime(date.getTime() + 3600 * 24 * 10);//有效期十天
        loginTicket.setExpired(date);
        loginTicket.setStatus(0);
        loginTicketDao.insertTicker(loginTicket);
        return loginTicket.getTicket();
    }
}
