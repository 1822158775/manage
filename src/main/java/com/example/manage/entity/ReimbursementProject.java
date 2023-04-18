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
 * @date 2023-04-11 15:27:20
 * 报销记录关联项目
 */

@Data
@ToString
@TableName(value = "reimbursement_project")
public class ReimbursementProject implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "name")
    public String name;//项目名称
    @TableField(value = "reimbursement_record_code")
    public String reimbursementRecordCode;//报销记录编码
    @TableField(value = "manage_management_id")
    public Integer manageManagementId;//项目
    @TableField(value = "amount")
    public Double amount;//扣费金额

    public ReimbursementProject() {
    }

    public ReimbursementProject(String name, String reimbursementRecordCode, Integer manageManagementId, Double amount) {
        this.name = name;
        this.reimbursementRecordCode = reimbursementRecordCode;
        this.manageManagementId = manageManagementId;
        this.amount = amount;
    }
}
