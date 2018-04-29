package com.zhou.wenda.service;


import com.zhou.wenda.Dao.FeedDao;
import com.zhou.wenda.domain.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FeedService {

    @Resource
    private FeedDao feedDao;

    public boolean addFeed(Feed feed){
        feedDao.addFeed(feed);
        return feed.getId()>0;
    }

    public Feed getFeedById(int id){
        return feedDao.getFeedById(id);
    }

    public List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count){
        return feedDao.selectUserFeeds(maxId, userIds, count);
    }
}
