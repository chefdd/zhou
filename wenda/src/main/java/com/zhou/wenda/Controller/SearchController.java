package com.zhou.wenda.Controller;

import com.zhou.wenda.domain.EntityType;
import com.zhou.wenda.domain.Question;
import com.zhou.wenda.domain.ViewObject;
import com.zhou.wenda.service.FollowService;
import com.zhou.wenda.service.QuestionService;
import com.zhou.wenda.service.SearchService;
import com.zhou.wenda.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private SearchService searchService;

    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @RequestMapping(value = {"/search"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String search(Model model,
                         @RequestParam("keyword") String keyword,
                         @RequestParam(value = "offset", defaultValue = "0") int offset,
                         @RequestParam(value = "count", defaultValue = "10") int count){
        try {
            List<Question> questionList = searchService.searchQuestion(keyword, offset, count,
                    "<font color = 'red'>", "</font>");
            logger.info("问题搜索出来的大小为： {}", questionList.size());

            List<ViewObject> vos = new ArrayList<>();
            for (Question question : questionList) {
                Question q = questionService.selectById(question.getId());
                ViewObject vo = new ViewObject();
                if (question.getContent() != null) {
                    q.setContent(question.getContent());
                    logger.info("搜索问题内筒为：{}", question.getContent());
                }
                if (question.getTitle() != null) {
                    q.setTitle(question.getTitle());
                    logger.info("搜索问题标题为：{}", question.getTitle());

                }
                vo.set("question", q);
                vo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
                vo.set("user", userService.getUserById(q.getUserId()));
                vos.add(vo);
            }
            model.addAttribute("vos", vos);
            model.addAttribute("keyword", keyword);

        }catch (Exception e){
            logger.error("搜索问题失败：" + e.getMessage());
        }
        return "result";
    }

}

