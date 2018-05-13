package com.zhou.wenda.Dao;

import com.zhou.wenda.domain.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDao {


    /**
     * 添加
     *
     * @param user
     * @return
     */
    @Insert("insert into user(name, password, salt, head_url) values (#{name}, #{password}, #{salt}, #{headUrl})")
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

    /**
     * 更改用户的密码
     * @param user
     * @return
     */
    @Update("update user set password = #{password} where id = #{id}")
    boolean updatePassword(User user);


    /**
     * find
     */
    @Select("select * from user where id = #{id}")
    User findById(int id);

    @Select("select * from user where name = #{name}")
    User findByName(String name);

    //修改用户密码
    @Update("update user set password = @{password} where id = @{userId}")
    boolean updatePwd(@Param("userId") int userId,@Param("password") String password);

    //修改用户头像图片
    @Update("update user set head_url = @{headUrl} where id = @{userId}")
    boolean updateHeadUrl(@Param("userId") int userId,@Param("headUrl") String headUrl);


}
