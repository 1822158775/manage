package com.example.manage.service.impl;

import com.example.manage.service.IRedisUtilService;
import com.example.manage.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @avthor 潘小章
 * @date 2023/10/16
 */
@Slf4j
@Service
public class IRedisUtilServiceImpl implements IRedisUtilService {
    @Resource
    private RedisUtil redisUtil;
    @Override
    public Object get(String key) {
        return redisUtil.get(key);
    }

    @Override
    public void set(String token_code, String token_code1) {
        redisUtil.set(token_code,token_code1);
    }
}
