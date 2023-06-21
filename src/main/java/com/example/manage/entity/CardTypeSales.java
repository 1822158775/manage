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
 * @date 2023-06-16 15:07:18
 * 信用卡关联权益
 */

@Data
@ToString
@TableName(value = "card_type_sales")
public class CardTypeSales implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "type")
    public String type;//权益名称
    @TableField(value = "state")
    public String state;//状态
    @TableField(value = "card_type_id")
    public Integer cardTypeId;//卡类型
    @TableField(value = "comment")
    public String comment;//备注
    @TableField(value = "alias")
    public String alias;//别名
}
