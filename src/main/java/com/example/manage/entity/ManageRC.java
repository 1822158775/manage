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
 * @date 2023-04-11 14:25:03
 * 报销类目关联特殊条件管理
 */

@Data
@ToString
@TableName(value = "manage_r_c")
public class ManageRC implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "category_coding")
    public String categoryCoding;//类目编码
    @TableField(value = "manage_condition")
    public Integer manageCondition;//特殊条件编码

    public ManageRC() {
    }

    public ManageRC(String categoryCoding, Integer manageCondition) {
        this.categoryCoding = categoryCoding;
        this.manageCondition = manageCondition;
    }
}
