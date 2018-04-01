package com.zhou.wenda.service;


import com.zhou.wenda.Dao.QuestionDao;
import com.zhou.wenda.domain.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private SensitiveService sensitiveService;

    //add
    public boolean addQuestion(Question question) {
        //html过滤
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));

        //敏感词过滤
        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));
        return questionDao.insertQuestion(question);
    }


    //find 最新的问题
    public List<Question> getLastestQuestions(int userId, int offset, int limit) {
        return questionDao.selectLatestQuestions(userId, offset, limit);
    }



    public void updateCommentCount(int id, int count) {
        questionDao.updateCommentCount(id, count);
    }


    public Question selectByid(int id) {
        return questionDao.findById(id);
    }



}
