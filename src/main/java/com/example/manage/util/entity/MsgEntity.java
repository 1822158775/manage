package com.example.manage.util.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @avthor 潘小章
 * @date 2022/4/14
 */
@Data
@ToString
public class MsgEntity implements Serializable {
    public static String CODE_SUCCEED = "成功";//成功
    public static String CODE_ALREADY_EXIST = "订单存在";//订单存在
    public static String CODE_ERROR = "请仔细检查提交的数据";//异常
    public static String CODE_ERROR_C = "服务器忙碌，请稍后再试";//异常
    public static String CODE_422 = "权限不足";//权限不足
    public static String CODE_300 = "登录超时";
}
