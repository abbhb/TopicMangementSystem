package com.qc.topicmanagementsystem.service.impl;

import com.qc.topicmanagementsystem.common.MyString;
import com.qc.topicmanagementsystem.pojo.User;
import com.qc.topicmanagementsystem.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class IRedisServiceImpl implements IRedisService {

    private final RedisTemplate redisTemplate;
    @Autowired
    public IRedisServiceImpl(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Object getHash(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key,hashKey);
    }

    @Override
    public void addToken(String uuid, User one, long l) {
        redisTemplate.opsForValue().set(MyString.pre_user_redis + uuid, one, l, TimeUnit.SECONDS);
    }

    @Override
    public Object getToken(String uuid) {
        return redisTemplate.opsForValue().get(MyString.pre_user_redis + uuid);
    }

    @Override
    public Long getTokenTTL(String uuid) {
        Long expire = redisTemplate.getExpire(MyString.pre_user_redis + uuid);
        return expire;
    }

    @Override
    public void setTokenTTL(String uuid, long l) {
        redisTemplate.expire(MyString.pre_user_redis + uuid,l , TimeUnit.SECONDS);
    }

    @Override
    public void hashPut(String key,String hashKey,Object object) {
        redisTemplate.opsForHash().put(key,hashKey,object);
    }

    @Override
    public void del(String uuid) {
        redisTemplate.delete(MyString.pre_user_redis + uuid);

    }

}
