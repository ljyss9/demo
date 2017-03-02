package com.ljy.Service;

import com.ljy.util.DemoUtil;
import com.ljy.util.RedisKeyUtil;
import com.ljy.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ljy on 2017/2/17.
 */

@Service
public class LikeService {

    @Autowired
    RedisUtil redisUtil;

    public int getLikeStatus(int userId,int entityType, int entityId){
          String keyValue = RedisKeyUtil.getLikeKey(entityId, entityType);
          if(redisUtil.sismember(keyValue ,String.valueOf(userId)))
                return 1;
           String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
           return redisUtil.sismember(disLikeKey, String.valueOf(userId)) ? -1 : 0;
    }

    public long addLike(int userId,int entityType, int entityId){
        String keyValue = RedisKeyUtil.getLikeKey(entityId, entityType);
        redisUtil.sadd(keyValue,String.valueOf(userId));
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
        redisUtil.srem(disLikeKey,String.valueOf(userId));
        return redisUtil.scare(keyValue);
    }

    public long disLike(int userId,int entityType, int entityId){
        String keyValue = RedisKeyUtil.getLikeKey(entityId, entityType);
        redisUtil.srem(keyValue,String.valueOf(userId));
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
        redisUtil.sadd(disLikeKey,String.valueOf(userId));
        return redisUtil.scare(keyValue);
    }

}
