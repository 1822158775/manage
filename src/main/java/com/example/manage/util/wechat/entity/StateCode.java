package com.example.manage.util.wechat.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @avthor 潘小章
 * @date 2023/3/28
 */
@Data
@ToString
public class StateCode {
    public String errcode;
    public String errmsg;
    public String msgid;
}
