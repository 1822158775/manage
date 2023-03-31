package com.example.manage.util.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @avthor 潘小章
 * @date 2023/3/31
 */

@Data
@ToString
public class Token {
    public Boolean success;
    public String message;
    public TokenResponse response;
}
