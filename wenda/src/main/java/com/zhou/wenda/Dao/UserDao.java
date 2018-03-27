package com.zhou.wenda.Dao;

import com.zhou.wenda.domain.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserDao {


    /**
     * 添加
     *
     * @param user
     * @return
     */
    @Insert("insert into user(name, password, salt, head_url) values (#{name}, #{password}, #{salt}, #{head_url})")
    boolean insertUser(User user);


    /**
     * 删除
     */
    @Delete("delete from user where id = #{id}")
    void deleted(int id);

    /**
     * 修改
     */
    @Update("update user set name = #{name} where id = #{id}")
    boolean updateName(User user);

    @Update("update user set password = #{password} where id = #{id}")
    boolean updatePassword(User user);


    /**
     * find
     */
    @Select("select * from user where id = #{id}")
    User findById(int id);

    @Select("select * from user where name = #{name}")
    User findByName(String name);
}
