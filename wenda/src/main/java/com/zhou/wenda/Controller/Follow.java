package com.zhou.wenda.Controller;


import com.zhou.wenda.domain.EntityType;
import com.zhou.wenda.service.FollowService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Host;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
@Slf4j
public class Follow {

    @Resource
    private FollowService followService;

    @Resource
    private HostHolder hostHolder;

    @RequestMapping(value = "/followUser", method = RequestMethod.POST)
    public String followUser(@RequestParam("userId") int userId) {
        try {
            if (hostHolder.getUser() == null){
                return "reglogin";
            }
            followService.follow(hostHolder.getUser().getId(), userId, EntityType.ENTITY_USER);

            return "redirect:/user/" + userId;

        } catch (Exception e) {
            log.error("关注失败" + e.getMessage());

        }
        return "reglogin";
    }
}
