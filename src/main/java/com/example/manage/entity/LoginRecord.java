package com.example.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @avthor 潘小章
 * @date 2023-07-14 15:14:23
 * 登入表
 */

@Data
@ToString
@TableName(value = "login_record")
public class LoginRecord implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//
    @TableField(value = "username")
    public String username;//登入账号
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "login_time")
    public String loginTime;//登入时间
    @TableField(value = "open_id")
    public String openId;//微信用户唯一标识
    @TableField(value = "phone_type")
    public String phoneType;//手机型号
    @TableField(value = "username_state")
    public String usernameState;//是否和当前登入的用户openId一致
    @TableField(value = "memory_size")
    public String memorySize;//设备内存大小

    public LoginRecord(Integer id, String username, String loginTime, String openId, String phoneType, String usernameState, String memorySize) {
        this.id = id;
        this.username = username;
        this.loginTime = loginTime;
        this.openId = openId;
        this.phoneType = phoneType;
        this.usernameState = usernameState;
        this.memorySize = memorySize;
    }

    public LoginRecord() {
    }
}
