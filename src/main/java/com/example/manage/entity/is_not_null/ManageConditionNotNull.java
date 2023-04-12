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
 * @date 2023-04-11 14:24:25
 * 特殊条件管理
 */

@Data
@ToString
public class ManageConditionNotNull implements Serializable {
    public String id;//数据编码
    public String name;//条件名称
    public String type;//类型，1：抵达时间
    public String conductType;//操作类型，1：通知
}
