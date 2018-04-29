package com.zhou.wenda.Dao;


import com.zhou.wenda.domain.Feed;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FeedDao {

    String TABLE_NAME = " feed ";
    String INSERT_FIELDS = " created_date, user_id, data, type ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    /**
     * 增加新鲜事
     * @param feed
     * @return
     */
    @Insert(value = "INSERT INTO "+TABLE_NAME+" ("+INSERT_FIELDS+") VALUES (#{createdDate}, #{userId}, #{data}, #{type})")
    int addFeed(Feed feed);

    /**
     * （采用推的模式）
     * @param id
     * @return
     */
    @Select(value = "SELECT "+SELECT_FIELDS+" from "+TABLE_NAME+" WHERE id = #{id}")
    Feed getFeedById(int id);

    /**
     * 动态sql：查询userId所关注的用户们的新鲜事（采用拉的模式）
     * @param maxId
     * @param userIds
     * @param count
     * @return
     */
    List<Feed> selectUserFeeds(@Param("maxId") int maxId,
                               @Param("userIds") List<Integer> userIds,
                               @Param("count") int count);
}
