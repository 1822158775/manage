package com.example.manage.util.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @avthor 潘小章
 * @date 2023/3/31
 */
@Data
@ToString
public class TokenResponse {
    public String access_token;
    public String openid;
}
