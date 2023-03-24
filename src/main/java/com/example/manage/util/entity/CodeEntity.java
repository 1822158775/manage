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
public class CodeEntity implements Serializable {
    public static String CODE_SUCCEED = "0";//成功
    public static String CODE_ERROR = "3";//异常
    public static String CODE_ALREADY_EXIST = "1";//订单存在
    public static String CODE_422 = "422";//权限不足
    public static String CODE_300 = "300";//登录失效
    public static String CODE_2 = "2";//积分兑换
}
