package com.zhou.wenda.Dao;


import com.zhou.wenda.domain.LoginTicket;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LoginTicketDao {

    /**
     * add
     * @return
     */
    @Insert("insert into login_ticket(user_id, ticket, expired, status) values (#{userId}, #{ticket}, #{expired}, #{status})")
    boolean insertTicker(LoginTicket loginTicket);


    @Update("update login_ticket set status = #{status} where ticket = #{ticket}")
    void updateStatus(@Param("ticket") String ticket, @Param("status") int status);

    @Select("select * from login_ticket where ticket = #{ticket}")
    LoginTicket findByTicket(String ticket);



}
