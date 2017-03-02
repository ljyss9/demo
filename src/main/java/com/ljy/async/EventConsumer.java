package com.ljy.async;

import com.alibaba.fastjson.JSON;
import com.ljy.util.RedisKeyUtil;
import com.ljy.util.RedisUtil;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ljy on 2017/2/19.
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware{
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    private Map<EventType,List<EventHandle>> config = new HashMap<EventType,List<EventHandle>>();
    private ApplicationContext applicationContext;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void afterPropertiesSet() throws Exception {
            Map<String ,EventHandle> beans = applicationContext.getBeansOfType(EventHandle.class);
            if(beans != null){
                for(Map.Entry<String,EventHandle> entry : beans.entrySet()){
                    List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                    for(EventType eventType :eventTypes){
                        if(!config.containsKey(eventType)){
                            config.put(eventType,new ArrayList<EventHandle>());
                        }
                        config.get(eventType).add(entry.getValue());
                    }
                }
            }
        Thread thread = new Thread((new Runnable() {
            @Override
            public void run() {
                while(true){
                    String key = RedisKeyUtil.getEventQueueKey();
                    List<String> messages = redisUtil.brpop(0, key);
                    for(String message:messages){
                        if(message.equals(key))
                            continue;

                        EventModel eventModel = JSON.parseObject(message ,EventModel.class);
                        if(!config.containsKey(eventModel.getType())){
                            logger.error("不能识别事件");
                            continue;
                        }
                        for(EventHandle handle :config.get(eventModel.getType()))
                        {
                            handle.doHandle(eventModel);
                        }
                    }
                }
            }
        }));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
