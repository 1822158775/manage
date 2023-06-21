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
 * @date 2023-06-16 15:07:18
 * 信用卡关联权益
 */

@Data
@ToString
public class CardTypeSalesNotNull implements Serializable {
    public String id;//数据编码
    public String type;//权益名称
    public String state;//状态
    public String cardTypeId;//卡类型
    public String comment;//备注
    public String alias;//别名

    public CardTypeSalesNotNull() {
    }

    public CardTypeSalesNotNull(String type, String cardTypeId) {
        this.type = type;
        this.cardTypeId = cardTypeId;
    }

    public CardTypeSalesNotNull(String id, String type, String state, String cardTypeId, String comment, String alias) {
        this.id = id;
        this.type = type;
        this.state = state;
        this.cardTypeId = cardTypeId;
        this.comment = comment;
        this.alias = alias;
    }
}
