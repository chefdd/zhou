package com.zhou.wenda.Dao;

import com.zhou.wenda.domain.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionDao {

    /**
     * 添加
     *
     * @param question
     * @return
     */
    @Insert(value = "insert into question(title, content, user_id, created_date,comment_count) values (#{title}, #{content}, #{userId}, #{createdDate}, #{commentCount})")
    boolean insertQuestion(Question question);

    /**
     * 删除
     */
    @Delete("delete from question where id = #{id}")
    void deleted(int id);

    /**
     * 修改
     */
    @Update(value = "update question set comment_count = #{count} where id = #{id}")
    boolean updateCommentCount(@Param("id") int id, @Param("count")int count);

   /**
     * find
     */
    @Select("select * from question where id = #{id}")
    Question findById(int id);


    @Select("select * from question order by created_date desc limit #{offset}, #{limit}")
    List<Question> selectLatestQuestions(@Param("userId") int userId,@Param("offset") int offset, @Param("limit") int limit);
}
