package com.example.manage.entity.is_not_null;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;

/**
 * @avthor 潘小章
 * @date 2023-07-14 15:14:23
 * 登入表
 */

@Data
@ToString
public class LoginRecordNotNull implements Serializable {
    public String id;//
    public String username;//登入账号
    public String loginTime;//登入时间
    public String openId;//微信用户唯一标识
    public String phoneType;//手机型号
    public String usernameState;//是否和当前登入的用户openId一致
}
