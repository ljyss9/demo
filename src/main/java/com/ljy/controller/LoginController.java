package com.ljy.controller;

import com.ljy.DAO.UserDAO;
import com.ljy.Service.UserService;
import com.ljy.util.DemoUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import java.util.Map;

/**
 * Created by ljy on 2017/2/14.
 */
@Controller
public class LoginController {

    @Autowired
    UserDAO userDao;
    @Autowired
    UserService userService;

    @RequestMapping(path = {"/reg/"} )
    @ResponseBody
    public String register(@RequestParam("username") String name,
                           @RequestParam("password") String password,
                           @RequestParam(value="rember" ,defaultValue = "0") int rember,
                           HttpServletResponse response){
        Map<String,Object> info = userService.register(name,password);
        if(info.containsKey("ticket")){
            Cookie cookie = new Cookie("ticket",info.get("ticket").toString());
            cookie.setPath("/");
            if (rember > 0) {
                cookie.setMaxAge(3600*24*5);
            }
            response.addCookie(cookie);
            return DemoUtil.getJSONString(0,"注册成功");
        }
        else{
            return DemoUtil.getJSONString(1,info);
        }

    }
    @RequestMapping(path = {"/login"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String Login(@RequestParam("username") String name,
                        @RequestParam("password") String password,
                        @RequestParam(value="rember", defaultValue = "0") int rememberme,
                        HttpServletResponse response){
        Map<String ,Object> result = userService.Login(name,password);
        if(result.containsKey("ticket")){
            Cookie cookie = new Cookie("ticket",result.get("ticket").toString());
            cookie.setPath("/");
            if (rememberme > 0) {
                cookie.setMaxAge(3600*24*5);
            }
            response.addCookie(cookie);
            return DemoUtil.getJSONString(0,"注册成功");
        }
        else{
            return DemoUtil.getJSONString(1,result);
        }
    }

    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }

}
