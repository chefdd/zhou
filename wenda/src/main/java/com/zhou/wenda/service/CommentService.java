package com.zhou.wenda.service;


import com.zhou.wenda.Dao.CommentDao;
import com.zhou.wenda.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private SensitiveService sensitiveService;

    public int insertComment(Comment comment) {
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));
        return commentDao.insertComment(comment) == true ? comment.getId() : 0;
    }

    /**
     * 根据实体类型获取对应实体的评论
     * @param entityId
     * @param entityType
     * @return
     */
    public List<Comment> selectCommentByEntity(int entityId, int entityType){
        return commentDao.findCommentByEntity(entityId, entityType);
    }

    /**
     * 根据实体类型获取对应实体的评论数量
     * @param entityId
     * @param entityType
     * @return
     */
    public int getCommentCount(int entityId,int entityType){
        return commentDao.getCommentCount(entityId, entityType);
    }


    public Comment getComment(int commentId){
        return commentDao.findById(commentId);
    }

    //delete comment
    public boolean updateStatus(int id) {
        return commentDao.updateCommentStatus(id, 1);
    }




}
