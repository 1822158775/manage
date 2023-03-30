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
 * @date 2023-03-30 16:23:52
 * 项目关联卡种
 */

@Data
@ToString
@TableName(value = "manage_card_type")
public class ManageCardType implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "manage_code")
    public String manageCode;//项目编码
    @TableField(value = "card_type_id")
    public Integer cardTypeId;//卡种类型编码

    public ManageCardType() {
    }

    public ManageCardType(String manageCode, Integer cardTypeId) {
        this.manageCode = manageCode;
        this.cardTypeId = cardTypeId;
    }
}
