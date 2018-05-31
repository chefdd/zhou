package com.zhou.wenda.Controller;


import com.zhou.wenda.domain.Message;
import com.zhou.wenda.domain.User;
import com.zhou.wenda.domain.ViewObject;
import com.zhou.wenda.service.MessageService;
import com.zhou.wenda.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@Slf4j
public class MessageController {

    @Resource
    private HostHolder hostHolder;

    @Resource
    private MessageService messageService;

    @Resource
    private UserService userService;

    /**
     * 发送message给指定用户
     *
     * @param model
     * @param touserName
     * @param content
     * @return
     */
    @RequestMapping(value = "/msg/addMessage", method = RequestMethod.POST)
    public String addMessage(Model model,
                             @RequestParam("toName") String touserName,
                             @RequestParam("content") String content) {
        try {
            if (hostHolder.getUser() == null) {
                return "/reglogin";
            } else {
                User targetUser = userService.findByName(touserName);
                if (targetUser == null){
                    model.addAttribute("msg", "用户名不存在");
                    return "index";
                }
                Message message = new Message();
                message.setContent(content);
                message.setCreatedDate(new Date());
                message.setFromId(hostHolder.getUser().getId());
                message.setToId(targetUser.getId());
                messageService.addMessage(message);
            }
        } catch (Exception e) {
            log.error("发送私信出错" + e.getMessage());
            return "redirect:";
        }
        return "redirect:/";
    }

    /**
     * message list
     * @param model
     * @return
     */
    @RequestMapping(value = "/msg/list", method = {RequestMethod.GET, RequestMethod.POST})
    public String messageList(Model model) {
        try {
            if (hostHolder.getUser() == null) {
                log.info("用户未登陆");
                return "/reglogin";
            } else{
                int userId = hostHolder.getUser().getId();
                List<Message> conversationsList = messageService.getConversationList(userId);
                log.info("查询出来的msg :{}", conversationsList);
                List<ViewObject> conversations = new ArrayList<>();

                for (Message message : conversationsList) {
                    ViewObject vo = new ViewObject();
                    vo.set("message", message);
                    int targetId = (message.getFromId() == userId ? message.getToId():message.getFromId());
                    vo.set("user", userService.getUserById(targetId));
                    vo.set("count", messageService.getConversationIdCount(message.getConversationId()));
                    conversations.add(vo);
                }

                model.addAttribute("conversations", conversations);
            }
        } catch (Exception e) {
            log.error("获取消息列表失败" + e.getMessage());

        }
        return "letter";
    }

    /**
     * 站内信详情
     * @param model
     * @param conversationId
     * @return
     */
    @RequestMapping(value = "/msg/detail", method = {RequestMethod.GET})
    public String getConversationDetail(Model model,
                                        @RequestParam("conversationId") String conversationId){

        try {
            List<Message> messageList = messageService.getConversationsDetailById(conversationId);
            List<ViewObject> messages = new ArrayList<>();

            for (Message message : messageList){
                ViewObject vo = new ViewObject();
                vo.set("message", message);
                vo.set("user", userService.getUserById(message.getFromId()));
                messages.add(vo);
            }

            model.addAttribute("messages", messages);
        }catch (Exception e){
            log.error("获取详情失败：" + e.getMessage());
        }
        return "letterDetail";
    }


}
