package com.ljy.async;

import com.alibaba.fastjson.JSONObject;
import com.ljy.util.RedisKeyUtil;
import com.ljy.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ljy on 2017/2/19.
 */

@Service
public class EventProducer {

    @Autowired
    RedisUtil redisUtil;

    public boolean fireEvent(EventModel eventModel){
        try{
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            redisUtil.lpush(key,json);
            return true;
        }catch(Exception e){
            return false;
        }
    }

}
