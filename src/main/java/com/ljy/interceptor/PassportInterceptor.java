package com.ljy.interceptor;

import com.ljy.DAO.LoginTicketDAO;
import com.ljy.DAO.UserDAO;
import com.ljy.Model.HostHolder;
import com.ljy.Model.LoginTicket;
import com.ljy.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by ljy on 2017/2/14.
 */
@Component
public class PassportInterceptor implements HandlerInterceptor{
    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private  HostHolder hostHolder;


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {


        String ticket = null;
        Cookie []cookies = httpServletRequest.getCookies();
        if(cookies == null)
            return true;
        for(Cookie cookie :cookies){
            if(cookie.getName().equals("ticket")) {
                ticket = cookie.getValue();
                break;
            }
        }
        if(ticket != null){
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            if(loginTicket == null ||  loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0)
                return true;
            else{
                User user = userDAO.selectById(loginTicket.getUserId());
                hostHolder.setUser(user);
            }

        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if(modelAndView != null && hostHolder.getUser() != null){
            modelAndView.addObject("user",hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}
