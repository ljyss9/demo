package com.ljy.Service;

import com.ljy.DAO.LoginTicketDAO;
import com.ljy.DAO.UserDAO;
import com.ljy.Model.LoginTicket;
import com.ljy.Model.User;
import com.ljy.util.DemoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by ljy on 2017/2/10.
 */
@Service
public class UserService {

    @Autowired
    private UserDAO userDAO ;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public User getUser(int id){
       return  userDAO.selectById(id);
    }

    public Map<String,Object> register(String userName, String password){
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(userName)) {
            map.put("msgname", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msgpwd", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(userName);

        if (user != null) {
            map.put("msgname", "用户名已经被注册");
            return map;
        }

        user = new User();
        user.setName(userName);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        user.setPassword(DemoUtil.MD5(password + user.getSalt()));
        userDAO.addUser(user);

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;

    }

    public Map<String ,Object> Login(String userName,String password){
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(userName)) {
            map.put("msgname", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msgpwd", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(userName);

        if (user == null) {
            map.put("msgname", "用户名不存在");
            return map;
        }

        if (!DemoUtil.MD5(password+user.getSalt()).equals(user.getPassword())) {
            map.put("msgpwd", "密码不正确");
            return map;
        }
        map.put("ticket",addLoginTicket(user.getId()));
        return map;
    }

    private String addLoginTicket(int userId) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }

    public void logout(String ticket){
         loginTicketDAO.updateStatus(ticket , 1);

    }

    private int getUserID(String name){
        return userDAO.selectByName(name).getId();
    }

}
