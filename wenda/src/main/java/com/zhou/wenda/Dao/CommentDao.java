package com.zhou.wenda.Dao;


import com.zhou.wenda.domain.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentDao {

    @Insert("insert into comment(content, user_id, entity_id, entity_type, created_date, status) values (#{content}, #{userId}, #{entityId}, #{entityType}, #{createdDate}, #{status})")
    boolean insertComment(Comment comment);

    /**
     * 删除的话将status改变，将评论保留在数据库，为了以后可以恢复数据
     * @param id
     * @param status
     * @return
     */
    @Update("update comment set status = #{status} where id = #{id}")
    boolean updateCommentStatus(@Param("id") int id, @Param("status") int status);


    @Select("select count(id) from comment where entity_id = #{entityId} and entity_type = #{entityType}")
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Select("select * from comment where id = #{commendId}")
    Comment findById(int commentId);


    @Select(value = "select * from comment where entity_id = #{entityId} and entity_type = #{entityType} order by created_date desc")
    List<Comment> findCommentByEntity(@Param("entityId") int entityId,
                                        @Param("entityType") int entityType);


}
