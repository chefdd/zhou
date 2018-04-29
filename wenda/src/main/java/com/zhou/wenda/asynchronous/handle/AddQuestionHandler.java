package com.zhou.wenda.asynchronous.handle;


import com.zhou.wenda.asynchronous.EventHandler;
import com.zhou.wenda.asynchronous.EventModel;
import com.zhou.wenda.asynchronous.EventType;
import com.zhou.wenda.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class AddQuestionHandler implements EventHandler {



    @Resource
    private SearchService searchService;

    @Override
    public void doHandle(EventModel model) {
        try {
            searchService.indexQuestion(model.getEntityId(), model.getExts().get("title"), model.getExts().get("content"));
        }catch (Exception e){
            log.error("增加问题索引失败：" + e.getMessage());
        }

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.ADD_QUESTION);
    }
}
