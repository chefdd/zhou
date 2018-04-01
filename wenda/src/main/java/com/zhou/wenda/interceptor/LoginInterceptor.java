package com.zhou.wenda.interceptor;


import com.zhou.wenda.Controller.HostHolder;
import com.zhou.wenda.Dao.LoginTicketDao;
import com.zhou.wenda.Dao.UserDao;
import com.zhou.wenda.domain.LoginTicket;
import com.zhou.wenda.domain.User;
import com.zhou.wenda.service.TicketService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private LoginTicketDao loginTicketDao;

    @Resource
    private UserDao userDao;

     @Resource
     private HostHolder hostHolder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("ticket".equals(cookie.getName())) {
                    ticket = cookie.getValue();
                    break;
                }
            }

        }
        if (ticket != null) {
            LoginTicket loginTicket = loginTicketDao.findByTicket(ticket);
            //检验ticket  不存在， 过期 无效
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {
                return true;
            }

            //user has login
            User user = userDao.findById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    //    System.out.println(hostHolder.getUser());
        if (modelAndView != null&& hostHolder.getUser() != null){
            //前端可以通过${user}直接访问hostHolder中的user信息
            modelAndView.addObject("user", hostHolder.getUser());
           // System.out.println(hostHolder.getUser());
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
