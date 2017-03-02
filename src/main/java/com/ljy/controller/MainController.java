package com.ljy.controller;

import com.ljy.Model.EntityType;
import com.ljy.Model.HostHolder;
import com.ljy.Model.News;
import com.ljy.Model.ViewObject;
import com.ljy.Service.LikeService;
import com.ljy.Service.NewsService;
import com.ljy.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljy on 2017/2/8.
 */

@Controller
public class MainController {

    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    private List<ViewObject> getNews(int userId, int offset , int limit){
        List<News> newsList = newsService.getLatesNews(userId, offset ,limit);
        int localUserID = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<ViewObject> vos  = new ArrayList<>();
        for(News news : newsList){
            ViewObject vo = new ViewObject();
            vo.set("news" , news);
            vo.set("user",userService.getUser(news.getUserId()));
            if( localUserID != 0){
                vo.set("like",likeService.getLikeStatus(localUserID, EntityType.ENTITY_NEWS,news.getId()));
            }
            else{
                vo.set("like",0);
            }
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(path = {"/","/index"},method = {RequestMethod.POST,RequestMethod.GET})
    public String index(Model model){
        model.addAttribute("vos",getNews(0,0,10));
        if(hostHolder != null){

        }
        return "home";
    }

    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.POST,RequestMethod.GET})
    public String userIndex(Model model,@PathVariable("userId") int userId)
    {
        model.addAttribute("vos",getNews(userId,0,10));
        return "home";
    }

    @RequestMapping(value = "/user/{username}/{userage}")
    @ResponseBody
    public String user(@PathVariable("username") String username,
                       @PathVariable("userage") int age,
                       @RequestParam("type") String type) {
        String s = "UserName : ";
        s += username;
        s += " Userage : ";
        s += age;
        s += "Type : ";
        s += type;
        return  s;
    }

    @RequestMapping("/ShowVm")
    public String showVM(Model model,@RequestParam("Give") String input){
        model.addAttribute("Give",input);
        model.addAttribute("Two","2");
        return "valueshow";
    }


    @RequestMapping("/request")
    public String request(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession session){

        RedirectView red = new RedirectView();
       String ret = "ContextPath: "+ request.getContextPath() + " <br>";
        //ret += "PathInfo : "+ request.getPathInfo() + "<br>";
        ret += "ServletPath : " + request.getServletPath();

        return ret;

    }
}
