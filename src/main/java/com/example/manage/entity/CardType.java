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
 * @date 2023-03-30 14:11:53
 * 卡种管理
 */

@Data
@ToString
@TableName(value = "card_type")
public class CardType implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "name")
    public String name;//卡的名称
    @TableField(value = "amount")
    public String amount;//金额
    @TableField(value = "type")
    public String type;//类型

    public CardType() {
    }

    public CardType(Integer id, String name, String amount, String type) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.type = type;
    }
}
