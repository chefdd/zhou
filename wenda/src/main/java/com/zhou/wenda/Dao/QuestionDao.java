package com.zhou.wenda.Dao;

import com.zhou.wenda.domain.Question;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface QuestionDao {

    /**
     * 添加
     *
     * @param question
     * @return
     */
    @Insert(value = "insert into question(title, content, userId, createDate,commentCount) values (#{title}, #{content}, #{userId}, #{createdDate}, #{commentCount})")
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
    boolean updateCommentCount(int id, int count);

   /**
     * find
     */
    @Select("select * from user where id = #{id}")
    Question findById(int id);


    @Select("select * from question where id = #{userId} limit #{offset}, #{limit}")
    List<Question> selectLatestQuestions(int userId,int offset, int limit);
}
