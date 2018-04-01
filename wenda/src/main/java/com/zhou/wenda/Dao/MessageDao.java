package com.zhou.wenda.Dao;


import com.zhou.wenda.domain.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessageDao {

    @Insert("insert into message(from_id, to_id, content, has_read, created_date, conversation_id) values(#{fromId}, #{toId}, #{content}, #{hasRead}, #{createdDate}, #{conversationId})")
    boolean insertMessage(Message message);


    @Select("select * from message where conversation_id = #{conversationId} order by created_date desc")
    List<Message> getConversationsDetailById(@Param("conversationId") String conversationId);


   /* @Select(value = "select * from (select * from message where from_id = #{userId} or to_id = #{userId} order by created_date desc) tt group by conversation_id,from_id,to_id,content,created_date,has_read order by created_date desc")
    List<Message> getConversationList(@Param("userId") int userId);
*/

    @Select(value = "select from_id, to_id, content, created_date, has_read, conversation_id , count(id) as id from (select * from message where from_id = #{userId} or to_id = #{userId} order by created_date desc) tt group by conversation_id,from_id,to_id,content,created_date,has_read order by created_date desc")
    List<Message> getConversationList(@Param("userId") int userId);
}