package com.zhou.wenda.Controller;


import com.zhou.wenda.asynchronous.EventModel;
import com.zhou.wenda.asynchronous.EventProducer;
import com.zhou.wenda.asynchronous.EventType;
import com.zhou.wenda.domain.EntityType;
import com.zhou.wenda.domain.Question;
import com.zhou.wenda.domain.User;
import com.zhou.wenda.domain.ViewObject;
import com.zhou.wenda.service.FollowService;
import com.zhou.wenda.service.QuestionService;
import com.zhou.wenda.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Host;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sun.misc.Request;

import javax.annotation.Resource;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class Follow {

    @Resource
    private FollowService followService;

    @Resource
    private HostHolder hostHolder;

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    @Resource
    private EventProducer eventProducer;

    @RequestMapping(value = "/followUser", method = RequestMethod.POST)
    public String followUser(@RequestParam("userId") int userId) {
        try {
            if (hostHolder.getUser() == null){
                return "reglogin";
            }
            followService.follow(hostHolder.getUser().getId(), userId, EntityType.ENTITY_USER);


            /**
             * 异步：关注用户后，发送私信
             */
            eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                    .setActorId(hostHolder.getUser().getId())
                    .setEntityId(userId)
                    .setEntityType(EntityType.ENTITY_USER)
                    .setEntityOwnerId(userId));
            return "redirect:/user/" + userId;

        } catch (Exception e) {
            log.error("关注失败" + e.getMessage());

        }
        return "reglogin";
    }

    /**
     * 取消关注用户
     * @param userId
     * @return
     */
    @RequestMapping(value = "/unfollowUser", method = RequestMethod.POST)
    public String unfollowUser(@RequestParam("userId") int userId) {
        try {
            if (hostHolder.getUser() == null){
                return "reglogin";
            }


            boolean ret = followService.unfollow(hostHolder.getUser().getId(), userId, EntityType.ENTITY_USER);


            return "redirect:/user/" + userId;

        } catch (Exception e) {
            log.error("关注失败" + e.getMessage());

        }
        return "reglogin";

    }

    /**
     * user关注问题
     * @param model
     * @param questionId
     * @return
     */
    @RequestMapping(value = "/followQuestion", method = RequestMethod.POST)
    public String followQues(Model model,
                             @RequestParam("questionId") int questionId) {
        try {
            if (hostHolder.getUser() == null){
                //login
                return "reglogin ";
            }
            Question question = questionService.selectById(questionId);
            if (question == null) {
                log.error("问题不存在");
            }


            followService.follow(hostHolder.getUser().getId(), questionId, EntityType.ENTITY_QUESTION);

            /**
             * 异步：关注问题后，发送私信
             */
            eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                    .setActorId(hostHolder.getUser().getId())
                    .setEntityId(questionId)
                    .setEntityType(EntityType.ENTITY_QUESTION)
                    .setEntityOwnerId(question.getUserId()));

            return "redirect:/question/" + questionId;

        } catch (Exception e) {
            log.error("关注问题失败" + e.getMessage() );

        }
        return "reglogin";
    }

    /**
     * 取消关注问题
     * @param model
     * @param questionId
     * @return
     */
    @RequestMapping(value = "/unfollowQuestion", method = RequestMethod.POST)
    public String unfollowQues(Model model,
                             @RequestParam("questionId") int questionId) {
        try {
            if (hostHolder.getUser() == null){
                //login
                return "reglogin ";
            }
            Question question = questionService.selectById(questionId);
            if (question == null) {
                log.error("问题不存在");
            }

            followService.unfollow(hostHolder.getUser().getId(), questionId, EntityType.ENTITY_QUESTION);

            return "redirect:/question/" + questionId;

        } catch (Exception e) {
            log.error("取消关注问题失败" + e.getMessage() );

        }
        return "reglogin";
    }

    /**
     * get all fans
     */
    @RequestMapping(value = "/user/{uid}/followers", method = RequestMethod.GET)
    public String getfollowers(Model model,
                               @PathVariable("uid") int userId) {
        List<Integer> followersList = followService.getFollows(EntityType.ENTITY_USER, userId, 0, 10);
        if (hostHolder.getUser() == null) {
            model.addAttribute("followers", getUsersInfo(0, followersList));

        } else{
            model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getId(),followersList));
        }
        model.addAttribute("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        model.addAttribute("curUser", userService.getUserById(userId));
        return "followers";
    }

    //all follows
    @RequestMapping(value = "/user/{uid}/followees", method = RequestMethod.GET)
    public String getAllFollowees(Model model,
                                  @PathVariable("uid") int userId) {
        List<Integer> followeeIds = followService.getFollowees(userId, EntityType.ENTITY_USER, 0, 10);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followees", getUsersInfo(hostHolder.getUser().getId(), followeeIds));
        } else {
            model.addAttribute("followees", getUsersInfo(0, followeeIds));
        }
        model.addAttribute("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        model.addAttribute("curUser", userService.getUserById(userId));
        return "followees";
    }

    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
        List<ViewObject> userInfoList = new ArrayList<>();
        for (Integer userId : userIds) {
            User user = userService.getUserById(userId);
            if (user == null) {
                continue;

            }
            ViewObject vo = new ViewObject();
            vo.set("user", user);
            vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
            vo.set("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
            if (localUserId != 0) {
                vo.set("followed", followService.isFollower(localUserId, EntityType.ENTITY_USER, userId));
            } else {
                vo.set("followed", false);
            }
            userInfoList.add(vo);
        }
        return userInfoList;
    }
}
