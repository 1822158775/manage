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
 * @date 2023-04-11 14:25:03
 * 报销类目关联特殊条件管理
 */

@Data
@ToString
public class ManageRCNotNull implements Serializable {
    public String id;//数据编码
    public String categoryCoding;//类目编码
    public String manageCondition;//特殊条件编码
}
