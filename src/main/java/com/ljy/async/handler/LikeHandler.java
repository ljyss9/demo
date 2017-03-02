package com.ljy.async.handler;

import com.ljy.Model.Message;
import com.ljy.Model.User;
import com.ljy.Service.MessageService;
import com.ljy.Service.UserService;
import com.ljy.async.EventHandle;
import com.ljy.async.EventModel;
import com.ljy.async.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by ljy on 2017/2/19.
 */

@Component
public class LikeHandler implements EventHandle{
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        User user = userService.getUser(model.getActorId());
        message.setToId(user.getId());
        message.setContent("用户" + user.getName() +
                " 赞了你的资讯,http://127.0.0.1:8080/news/"
                + String.valueOf(model.getEntityId()));
        // SYSTEM ACCOUNT
        message.setFromId(3);
        message.setCreatedDate(new Date());
        messageService.addMessage(message);
    }

    @Override
    public List<EventType>  getSupportEventTypes(){
        return Arrays.asList(EventType.LIKE);
    }

}
