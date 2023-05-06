package com.qc.topicmanagementsystem.service;

import com.qc.topicmanagementsystem.pojo.Permission;
import com.qc.topicmanagementsystem.pojo.User;

public interface IRedisService {
    Object getHash(String key, String hashKey);

    void addToken(String uuid, User one, long l);

    Object getToken(String uuid);

    Long getTokenTTL(String uuid);

    void setTokenTTL(String uuid, long l);

    void hashPut(String key,String hashKey,Object object) ;

    void del(String uuid);
}
