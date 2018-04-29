package com.zhou.wenda.Controller;

import com.zhou.wenda.asynchronous.EventModel;
import com.zhou.wenda.asynchronous.EventProducer;
import com.zhou.wenda.asynchronous.EventType;
import com.zhou.wenda.domain.Comment;
import com.zhou.wenda.domain.EntityType;
import com.zhou.wenda.service.CommentService;
import com.zhou.wenda.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
public class LIkeController {

    @Resource
    private HostHolder hostHolder;

    @Resource
    private LikeService likeService;

    @Resource
    private CommentService commentService;

    @Autowired
    private EventProducer eventProducer;

    /**
     * 点赞
     * @param model
     * @param commentId
     * @return
     */
    @RequestMapping(value = "/like", method = {RequestMethod.POST})
    public String like(Model model, @RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null){
            return "reglogin";
        }
        Comment comment = commentService.getComment(commentId);

        /**
         * 异步：点赞后发送私信
         */
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(commentId)
                .setEntityType(EntityType.ENTITY_COMMENT)
                .setEntityOwnerId(comment.getUserId())
                .setExt("questionId", String.valueOf(comment.getEntityId())));
        long likeCount = likeService.likeCount(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        model.addAttribute("likeCount", likeCount);
        return "redirect:/question/" + comment.getEntityId();
    }

    @RequestMapping(value = "/dislike", method = {RequestMethod.POST})
    public String dislike(Model model, @RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null){
            return "reglogin";
        }
        Comment comment = commentService.getComment(commentId);
        long disCount = likeService.dislikeCount(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        model.addAttribute("likeCount", disCount);
        return "redirect:/question/" + comment.getEntityId();
    }
}
