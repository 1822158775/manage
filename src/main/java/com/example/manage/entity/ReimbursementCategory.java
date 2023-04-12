package com.example.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;
import java.util.Date;

/**
 * @avthor 潘小章
 * @date 2023-04-11 15:26:31
 * 报销记录关联类目
 */

@Data
@ToString
@TableName(value = "reimbursement_category")
public class ReimbursementCategory implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "reimbursement_record_code")
    public String reimbursementRecordCode;//报销记录编码
    @TableField(value = "reimbursement_category_id")
    public Integer reimbursementCategoryId;//报销种类编码
    @TableField(value = "amout")
    public Double amout;//金额
    @TableField(value = "name")
    public String name;//类目名称
    @TableField(value = "reimbursement_type")
    public String reimbursementType;//报销类型
    @TableField(value = "occurrence_time")
    public Date occurrenceTime;//发生时间

    public ReimbursementCategory() {
    }

    public ReimbursementCategory(String reimbursementRecordCode, Integer reimbursementCategoryId, Double amout, String name, String reimbursementType ,Date occurrenceTime) {
        this.reimbursementRecordCode = reimbursementRecordCode;
        this.reimbursementCategoryId = reimbursementCategoryId;
        this.amout = amout;
        this.name = name;
        this.reimbursementType = reimbursementType;
        this.occurrenceTime = occurrenceTime;
    }
}
