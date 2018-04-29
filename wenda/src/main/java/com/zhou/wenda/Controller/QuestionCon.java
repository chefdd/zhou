package com.zhou.wenda.Controller;


import com.zhou.wenda.domain.Comment;
import com.zhou.wenda.domain.EntityType;
import com.zhou.wenda.domain.Question;
import com.zhou.wenda.domain.ViewObject;
import com.zhou.wenda.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@Slf4j
public class QuestionCon {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    @Resource
    private CommentService commentService;

    @Resource
    private HostHolder hostHolder;

    @Resource
    private LikeService likeService;

    @Resource
    private FollowService followService;

    /**
     * add question
     *
     * @param title
     * @param content
     * @return
     */
    @RequestMapping(value = "/question/add", method = {RequestMethod.POST, RequestMethod.GET})
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content) {
        try {
            Question question = new Question(title, content, new Date(), 0);

            if (hostHolder.getUser() == null) {
                return "reglogin";
            } else {
                question.setUserId(hostHolder.getUser().getId());
            }
            if (questionService.addQuestion(question)) {
                /**
                 * 发起问题成功，异步事件
                 */
            }


            return "redirect:/";

        } catch (Exception e) {
            log.error("提问失败" + e.getMessage());

        }

        return "redirect:/";
    }

    @RequestMapping(value = "/question/{qid}", method = RequestMethod.GET)
    public String DetailQuestion(Model model,
                                 @PathVariable("qid") int questionId) {
        Question question = questionService.selectById(questionId);

        //System.out.println("question : =====" + question);
        List<Comment> commentList = commentService.selectCommentByEntity(questionId, EntityType.ENTITY_QUESTION);
        List<ViewObject> commentview = new ArrayList<>();
        for (Comment comment : commentList) {
            ViewObject vo = new ViewObject();
            vo.set("comment", comment);
            if (hostHolder.getUser() != null) {
                //设置点赞状态
                vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));

            } else {
                vo.set("liked", 0);

            }
            vo.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
            vo.set("user", userService.getUserById(comment.getUserId()));

            commentview.add(vo);
        }
        if (hostHolder.getUser() != null) {
            model.addAttribute("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId));
        } else{
            model.addAttribute("followed", false);

        }
        model.addAttribute("question", question);
        model.addAttribute("questionUser", userService.getUserById(question.getUserId()));

        model.addAttribute("comments", commentview);
        model.addAttribute("user", userService.getUserById(question.getUserId()));
        //System.out.println(userService.getUserById(question))
        return "detail";
    }



}


