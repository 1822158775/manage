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
import java.util.Date;

/**
 * @avthor 潘小章
 * @date 2023-10-31 09:24:57
 * 解绑记录表
 */

@Data
@ToString
@TableName(value = "unbind_record")
public class UnbindRecord implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//
    @TableField(value = "username")
    public String username;//解绑账号
    @TableField(value = "front_token_code")
    public String frontTokenCode;//解绑前秘钥
    @TableField(value = "after_token_code")
    public String afterTokenCode;//解绑后秘钥
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "unbinding_time")
    public Date unbindingTime;//解绑时间
    @TableField(value = "video_url")
    public String videoUrl;//视频路径
    @TableField(exist = false)
    public String token_code;//秘钥
}
