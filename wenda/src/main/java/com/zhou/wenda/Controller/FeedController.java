package com.zhou.wenda.Controller;

import com.zhou.wenda.config.RedisKey;
import com.zhou.wenda.domain.EntityType;
import com.zhou.wenda.domain.Feed;
import com.zhou.wenda.service.FeedService;
import com.zhou.wenda.service.FollowService;
import com.zhou.wenda.service.RedJedis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Controller
public class FeedController {

    @Resource
    private HostHolder hostHolder;

    @Resource
    private FeedService feedService;

    @Autowired
    FollowService followService;

    @Autowired
    private RedJedis jedisAdapter;

    /**
     * 采用拉的模式，直接从数据库中获取新鲜事
     * @param model
     * @return
     */
    @RequestMapping(value = {"/pullfeeds"})
    private String getPullFeeds(Model model){

        int localUserId = hostHolder.getUser() == null ? 0:hostHolder.getUser().getId();

        List<Integer> followees = new ArrayList<>();
        if (localUserId != 0){
            //找到userId所有关注的人
            followees = followService.getFollowees(localUserId, EntityType.ENTITY_USER, Integer.MAX_VALUE);
        }
        //获取所有关注的人的新鲜事
        List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followees, 10);
        log.info("f新鲜事大小:{}", feeds.size());

        model.addAttribute("feeds", feeds);
        return "feeds";
    }


}

