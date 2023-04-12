package com.example.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;

/**
 * @avthor 潘小章
 * @date 2023-04-11 14:24:24
 * 特殊条件管理
 */

@Data
@ToString
@TableName(value = "manage_condition")
public class ManageCondition implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "name")
    public String name;//条件名称
    @TableField(value = "type")
    public String type;//类型，1：抵达时间
    @TableField(value = "conduct_type")
    public String conductType;//操作类型，1：通知
}
