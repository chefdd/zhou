package com.zhou.wenda.Controller;


import com.zhou.wenda.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@Slf4j
public class RegisterAndLoginController {

    @Resource
    private UserService userService;


    @Resource
    private HostHolder hostHolder;

    /**
     * 登录注册页面
     * @param model
     * @param next
     * @return
     */
    @RequestMapping(value = "/reglogin", method = {RequestMethod.GET})
    public String relogin(Model model,
                          @RequestParam(value = "next",required = false) String next) {
        model.addAttribute("next", next);
        return "reglogin";
    }


    /**
     * reg
     * @param model
     * @param username
     * @param password
     * @param next
     * @param remember
     * @param response
     * @return
     */
    @RequestMapping(value = "/reg/", method = {RequestMethod.POST, RequestMethod.GET})
    public String register(Model model,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam(value = "next", required = false) String next,
                           @RequestParam(value = "rememver", defaultValue = "false") boolean remember,
                           HttpServletResponse response) {
        try {
            Map<String, String> map = userService.register(username, password);
            //检查ticket，没有就直接返回登录页面
            if (!map.containsKey("ticket")) {
                model.addAttribute("msg", map.get("msg"));
                return "reglogin";
            } else{
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                if (remember) {
                    cookie.setMaxAge(3600 * 24 * 7);//7 天

                }
                response.addCookie(cookie);

            }
            if (StringUtils.isEmpty(next)) {
                return "redirect:/";
            } else {
                return "redirect:" + next;
            }

        } catch (Exception e) {
            log.error("注册异常" + e.getMessage());
            return "reglogin";
        }
    }

    /**
     * login
     * @param model
     * @param username
     * @param password
     * @param next
     * @param remember
     * @param response
     * @return
     */
    @RequestMapping(value = "/login/", method = {RequestMethod.POST})
    public String login(Model model,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam(value = "next", required = false) String next,
                           @RequestParam(value = "rememver", defaultValue = "false") boolean remember,
                           HttpServletResponse response) {
        try {
            Map<String, String> map = userService.login(username, password);
            //检查ticket，没有就直接返回登录页面
            if (!map.containsKey("ticket")) {
                model.addAttribute("msg", map.get("msg"));
                return "reglogin";
            } else{
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                if (remember) {
                    cookie.setMaxAge(3600 * 24 * 7);//7 天

                }
                cookie.setPath("/");
                response.addCookie(cookie);

            }
            if (StringUtils.isEmpty(next)) {
                return "redirect:/";
            } else {
                return "redirect:" + next;
            }

        } catch (Exception e) {
            log.error("登录异常" + e.getMessage());
            return "reglogin";
        }
    }

    /**
     * 用户退出网站
     * @param ticket
     * @return
     */
    @RequestMapping(value = {"/logout"}, method = {RequestMethod.GET})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }

    /**
     * 用户修改密码
     * @param model
     * @param next
     * @return
     */
    @RequestMapping(value = "/changepwd/", method = RequestMethod.GET)
    public String changePwd(Model model,
                            @RequestParam(value = "next", required = false) String next){
        model.addAttribute("next", next);
        return "changepwd";
    }


    @RequestMapping(value = "/pwd/", method = {RequestMethod.POST,RequestMethod.GET})
    public String changePwd(Model model,
                            @RequestParam(value = "password") String password,
                            @RequestParam(value = "usernamepwd") String usernamepwd){
        System.out.println(password);
        System.out.println(usernamepwd);

        userService.updatepwd(hostHolder.getUser().getId(), usernamepwd, password);

        return "redirect:/";
    }
}
