package com.example.manage.service;

/**
 * @avthor 潘小章
 * @date 2023/10/16
 */

public interface IRedisUtilService {
    Object get(String key);
    void set(String token_code, String token_code1);
}
