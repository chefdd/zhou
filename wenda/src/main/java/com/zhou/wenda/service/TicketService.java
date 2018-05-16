package com.zhou.wenda.service;


import com.zhou.wenda.Dao.LoginTicketDao;
import com.zhou.wenda.domain.LoginTicket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@Slf4j
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
        log.info("现在的时间:{}", date);

        date.setTime(date.getTime() + 1000 * 30 * 60);//有效期30分钟 毫秒
        log.info("设置的时间为expired:{}", date);
        loginTicket.setExpired(date);
        loginTicket.setStatus(0);
        loginTicketDao.insertTicker(loginTicket);
        return loginTicket.getTicket();
    }
}
