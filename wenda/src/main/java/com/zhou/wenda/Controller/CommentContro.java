package com.zhou.wenda.Controller;


import com.zhou.wenda.domain.Comment;
import com.zhou.wenda.domain.EntityType;
import com.zhou.wenda.service.CommentService;
import com.zhou.wenda.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Date;

@Controller
@Slf4j
public class CommentContro {


    @Resource
    private CommentService commentService;

    @Resource
    private HostHolder hostHolder;

    @Resource
    private QuestionService questionService;


    @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    public String addComment(@RequestParam("questionId") Integer questionid,
                             @RequestParam("content") String content) {
        try {
            Comment comment = new Comment();
            comment.setContent(content);
            if (hostHolder.getUser() == null){
                return "reglogin";
            } else{
                comment.setUserId(hostHolder.getUser().getId());

            }
            comment.setEntityId(questionid);
            comment.setCreatedDate(new Date());
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            commentService.insertComment(comment);

            int commentCount = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            log.info("comment entityId "+comment.getEntityId() + comment.getId());
            questionService.updateCommentCount(comment.getId(), commentCount);

        } catch (Exception e) {
            log.error("添加评论失败" + e.getMessage());

        }

        return "redirect:/question/" + questionid;
    }


}
