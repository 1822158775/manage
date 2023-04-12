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
 * @date 2023-04-11 15:25:56
 * 报销记录抄送人
 */

@Data
@ToString
@TableName(value = "reimbursement_copy")
public class ReimbursementCopy implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "reimbursement_record_code")
    public String reimbursementRecordCode;//报销记录编码
    @TableField(value = "personnel_code")
    public String personnelCode;//资源代码
    @TableField(value = "personnel_name")
    public String personnelName;//审核人名称
    @TableField(value = "copy_time")
    public String copyTime;//抄送时间
    @TableField(exist = false)
    public Integer sysPersonnelId;//人员编码

    public ReimbursementCopy() {
    }

    public ReimbursementCopy(String reimbursementRecordCode, String personnelCode, String personnelName) {
        this.reimbursementRecordCode = reimbursementRecordCode;
        this.personnelCode = personnelCode;
        this.personnelName = personnelName;
    }
}
