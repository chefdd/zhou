package com.zhou.wenda.Dao;


import com.zhou.wenda.domain.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessageDao {

    @Insert("insert into message(from_id, to_id, content,, has_read, conversation_id, created_date) values(#{from_id}, #{to_id}, #{content},, #{has_read}, #{conversation_id}, #{created_date})")
    boolean insertMessage(Message message);

    @Select("select * from message where conversation_id = #{conversationId} oreder by created_date desc")
    List<Message> getConversationsDetailById(@Param("conversationId") String conversationId);


    @Select(value = "select * from (select * from message where from_id = #{userId} or to_id = #{userId} order by created_date desc)  group by conversation_id,from_id,to_id,content,created_date,has_read order by created_date desc")
    List<Message> getConversationList(@Param("userId") int userId);
}