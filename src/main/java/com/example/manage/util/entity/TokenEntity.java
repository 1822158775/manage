package com.example.manage.util.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @avthor 潘小章
 * @date 2022/4/15
 */
@Data
@ToString
public class TokenEntity {
    public String id;//用户唯一标识
    public String userName;//用户名称
    public Date userAddTime;//用户登录时间
    public Date userRemove;//用户token过期时间
    public Object userRole;//用户的角色

    public TokenEntity() {
    }

    public TokenEntity(String id, String userName, Date userAddTime, Date userRemove, Object userRole) {
        this.id = id;
        this.userName = userName;
        this.userAddTime = userAddTime;
        this.userRemove = userRemove;
        this.userRole = userRole;
    }
}
