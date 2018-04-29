package com.zhou.wenda.Controller;


import com.fasterxml.jackson.databind.ser.VirtualBeanPropertyWriter;
import com.zhou.wenda.domain.*;
import com.zhou.wenda.service.CommentService;
import com.zhou.wenda.service.FollowService;
import com.zhou.wenda.service.QuestionService;
import com.zhou.wenda.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Resource
    private HostHolder hostHolder;

    @Resource
    private UserService userService;

    @Resource
    private QuestionService questionService;

    @Resource
    private CommentService commentService;

    @Resource
    private FollowService followService;

    /**
     * 首页， 展示问题，
     * @param model
     * @return
     */
    @RequestMapping(value = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model) {
        model.addAttribute("vos", getQuestions(0, 0, 10));
        return "index";
    }


    /**
     * user page
     * @param model
     * @param userId
     * @return
     */
    @RequestMapping(value = {"/user/{userId}"}, method = RequestMethod.GET)
    public String userIndex(Model model,
                            @PathVariable("userId") int userId) {
        User user = userService.getUserById(userId);
        ViewObject vo = new ViewObject();
        vo.set("user", user);
        vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        vo.set("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        if (hostHolder.getUser() != null){
            vo.set("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId));
        }else {
            vo.set("followed", false);
        }
        model.addAttribute("profileUser", vo);

        return "profile";
    }

    private List<ViewObject> getQuestions(int userId, int offset, int limit) {
        List<Question> questionList = questionService.getLastestQuestions(userId, offset, limit);
      //  System.out.println("questionList" + questionList);
        List<ViewObject> vos = new ArrayList<>();
        for (Question question : questionList) {
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("user", userService.getUserById(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }



}
