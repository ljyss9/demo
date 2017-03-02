package com.ljy.controller;

import com.ljy.Model.EntityType;
import com.ljy.Model.HostHolder;
import com.ljy.Model.News;
import com.ljy.Service.LikeService;
import com.ljy.Service.NewsService;
import com.ljy.async.EventModel;
import com.ljy.async.EventProducer;
import com.ljy.async.EventType;
import com.ljy.util.DemoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ljy on 2017/2/17.
 */

@Controller
public class LikeController {

    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    NewsService newsService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/like"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String like(@RequestParam("newId") int newsId){
        long likeCount = likeService.addLike(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS,newsId);
        News news = newsService.getById(newsId);
        newsService.updateLikeCount(newsId,(int) likeCount);
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId()).setEntityId(newsId)
                .setEntityType(EntityType.ENTITY_NEWS).setEntityOwnerId(news.getUserId()));

        return DemoUtil.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("newId") int newsId) {
        long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS, newsId);
        // 更新喜欢数
        newsService.updateLikeCount(newsId, (int) likeCount);
        return DemoUtil.getJSONString(0, String.valueOf(likeCount));
    }

}
