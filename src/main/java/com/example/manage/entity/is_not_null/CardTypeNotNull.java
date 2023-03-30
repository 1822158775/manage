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
 * @date 2023-03-30 14:11:53
 * 卡种管理
 */

@Data
@ToString
public class CardTypeNotNull implements Serializable {
    public String id;//数据编码
    public String name;//卡的名称
    public String amount;//金额
    public String type;//类型
}
