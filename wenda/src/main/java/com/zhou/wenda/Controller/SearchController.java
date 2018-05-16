package com.zhou.wenda.Controller;

import com.zhou.wenda.domain.EntityType;
import com.zhou.wenda.domain.Question;
import com.zhou.wenda.domain.ViewObject;
import com.zhou.wenda.service.FollowService;
import com.zhou.wenda.service.QuestionService;
import com.zhou.wenda.service.SearchService;
import com.zhou.wenda.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class SearchController {

    @Resource
    private HostHolder hostHolder;

    @Resource
    private SearchService searchService;

    @Resource
    private FollowService followService;

    @Resource
    private UserService userService;

    @Resource
    private QuestionService questionService;

    @RequestMapping(value = {"/search"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String search(Model model,
                         @RequestParam("keyword") String keyword,
                         @RequestParam(value = "offset", defaultValue = "0") int offset,
                         @RequestParam(value = "count", defaultValue = "10") int count){
        try {
            List<Question> questionList = searchService.searchQuestion(keyword, offset, count,
                    "<font color = 'red'>", "</font>");
            log.info("问题搜索出来的大小为： {}", questionList.size());

            List<ViewObject> vos = new ArrayList<>();
            for (Question question : questionList) {
                Question q = questionService.selectById(question.getId());
                ViewObject vo = new ViewObject();
                if (question.getContent() != null) {
                    if (question.getContent().contains("src=\"http")) {
                        String content = question.getContent().replaceAll("src=\"http", " class=\"img-responsive center-block\" src=\"http");
                        question.setContent(content);

                    }
                    q.setContent(question.getContent());
                    log.info("搜索问题内筒为：{}", question.getContent());
                }
                if (question.getTitle() != null) {
                    q.setTitle(question.getTitle());
                    log.info("搜索问题标题为：{}", question.getTitle());

                }
                vo.set("question", q);
                vo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
                vo.set("user", userService.getUserById(q.getUserId()));
                vos.add(vo);
            }
            log.info("vos:{}, keyword:{}", vos, keyword);
            model.addAttribute("vos", vos);
            model.addAttribute("keyword", keyword);

        }catch (Exception e){
            log.error("搜索问题失败：" + e.getMessage());
        }
        return "result";
    }

}

