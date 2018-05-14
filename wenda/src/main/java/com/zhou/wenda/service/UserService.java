package com.zhou.wenda.service;


import com.zhou.wenda.Dao.LoginTicketDao;
import com.zhou.wenda.Dao.UserDao;
import com.zhou.wenda.domain.User;
import com.zhou.wenda.utils.WendaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginTicketDao loginTicketDao;

    @Autowired
    TicketService ticketService;

    public Map<String, String> register(String username, String password){
        Map<String, String> map = new HashMap<>();

        if (StringUtils.isEmpty(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }

        if (StringUtils.isEmpty(password)){
            map.put("msg", "密码不能为空");
            return map;
        }

        if (password.length() < 6 || password.length() > 12){
            map.put("msg", "密码长度不合法(5~12位）");
            return map;
        }
        User user = userDao.findByName(username);
        if (user!=null){
            map.put("msg","该用户名已经被注册！");
            return map;
        }


        //用户注册
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setPassword(WendaUtil.MD5(password + user.getSalt()));

        user.setHeadUrl(String.format("http://p61aboe8i.bkt.clouddn.com/%d.jpg", new Random().nextInt(23)));
        userDao.insertUser(user);
        /**
         * 注册成功，下发t票
         */
        String ticket = ticketService.addTicket(user.getId());
        map.put("ticket", ticket);
        return map;


    }


    public Map<String, String> login(String username, String password){
        Map<String, String> map = new HashMap<>();

        if (StringUtils.isEmpty(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }

        if (StringUtils.isEmpty(password)){
            map.put("msg", "密码不能为空");
            return map;
        }


        User user = userDao.findByName(username);
        if (user==null){
            map.put("msg","用户名不存在！");
            return map;
        }


       // System.out.println("zhoumima" + WendaUtil.MD5(password + user.getSalt()));

        if (!WendaUtil.MD5(password + user.getSalt()).equals(user.getPassword())) {
            map.put("msg", "密码错误");
            return map;
        }

        //用户login

        String ticket = ticketService.addTicket(user.getId());
        map.put("ticket", ticket);
        return map;


    }

    //退出，将ticket的status修改为1,无效
    public void logout(String ticket){
        loginTicketDao.updateStatus(ticket,1);
    }


    public User getUserById(int id) {
        return userDao.findById(id);
    }

    public User findByName(String username) {
        return userDao.findByName(username);
    }



    /**
     * 更新用户的密码
     * @param userId
     * @param pastPwd
     * @param nowPwd
     * @return信息只是用户是否更改成功
     */
    public Map<String, String> updatepwd(int userId, String pastPwd, String nowPwd) {
        User user = userDao.findById(userId);
        Map<String, String> map = new HashMap<>();


        String password = WendaUtil.MD5(pastPwd + user.getSalt());
        if (user.getPassword().equals(password)) {
            log.info("原密码输入没错，开始更新密码");
            //update password
            user.setPassword(WendaUtil.MD5(nowPwd + user.getSalt()));
            boolean flag = userDao.updatePassword(user);
            if (!flag) {
                map.put("msg", "更新密码的时候出错");
                return map;
            }

        } else{
            log.info("原密码不对，请重新输入");
            map.put("msg", "原密码不对，请重新输入");
            return map;

        }
        log.info("更新密码成功");
        return map;
    }

}
