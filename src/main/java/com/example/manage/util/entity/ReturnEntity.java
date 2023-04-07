package com.example.manage.util.entity;

import com.example.manage.util.PanXiaoZhang;
import lombok.Data;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

/**
 * @avthor 潘小章
 * @date 2022/4/14
 */
@Data
@ToString
public class ReturnEntity<T> implements Serializable {
    private String code;
    private T value;
    private T data;
    private String token;
    private String msg;
    private Integer count;
    private Boolean state;
    private HttpServletRequest request;

    public ReturnEntity() {
    }

    public ReturnEntity(Boolean state) {
        this.state = state;
    }

    public ReturnEntity(String code, T value, String token, String msg, Integer count, Boolean state) {
        this.code = code;
        this.value = value;
        this.token = token;
        this.msg = msg;
        this.count = count;
        this.state = state;
    }

    public ReturnEntity(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ReturnEntity(String code, T value, String msg) {
        this.code = code;
        this.value = value;
        this.msg = msg;
    }

    public ReturnEntity(String code, T value, HttpServletRequest request, String msg) {
        this.code = code;
        this.value = value;
        this.token = PanXiaoZhang.judgmentToken(request);
        this.msg = msg;
    }


    public ReturnEntity(String code, T value, HttpServletRequest request, String msg, Integer count) {
        this.code = code;
        this.value = value;
        this.token = PanXiaoZhang.judgmentToken(request);
        this.msg = msg;
        this.count = count;
    }

    public ReturnEntity(String code, T value, T data, HttpServletRequest request, String msg, Integer count) {
        this.code = code;
        this.value = value;
        this.data = data;
        this.token = PanXiaoZhang.judgmentToken(request);
        this.msg = msg;
        this.count = count;
    }

    public ReturnEntity(String code, T value, String token, String msg, Integer count, Boolean state, HttpServletRequest request) {
        this.code = code;
        this.value = value;
        this.token = token;
        this.msg = msg;
        this.count = count;
        this.state = state;
        this.request = request;
    }
}
