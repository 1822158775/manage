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
 * @date 2023-04-11 15:26:31
 * 报销记录关联类目
 */

@Data
@ToString
public class ReimbursementCategoryNotNull implements Serializable {
    public String id;//数据编码
    public String reimbursementRecordCode;//报销记录编码
    public String reimbursementCategoryId;//报销种类编码
    public String amout;//金额
    public String name;//类目名称
    public String reimbursementType;//报销类型
}
