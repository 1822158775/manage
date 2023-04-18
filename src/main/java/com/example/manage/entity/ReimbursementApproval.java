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
 * @date 2023-04-11 15:22:19
 * 报销记录关联审批人进行审批
 */

@Data
@ToString
@TableName(value = "reimbursement_approval")
public class ReimbursementApproval implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "reimbursement_record_code")
    public String reimbursementRecordCode;//报销记录编码
    @TableField(value = "personnel_code")
    public String personnelCode;//资源代码
    @TableField(value = "personnel_name")
    public String personnelName;//审核人名称
    @TableField(value = "number")
    public Integer number;//审批顺序
    @TableField(value = "approval_time")
    public String approvalTime;//审批时间
    @TableField(value = "approval_state")
    public String approvalState;//审批状态
    @TableField(value = "remark")
    public String remark;//备注
    @TableField(exist = false)
    public Integer sysPersonnelId;//人员编码

    public ReimbursementApproval() {
    }

    public ReimbursementApproval(String approvalTime, String approvalState, String remark) {
        this.approvalTime = approvalTime;
        this.approvalState = approvalState;
        this.remark = remark;
    }

    public ReimbursementApproval(String reimbursementRecordCode, String personnelCode, String personnelName, Integer number, String approvalState) {
        this.reimbursementRecordCode = reimbursementRecordCode;
        this.personnelCode = personnelCode;
        this.personnelName = personnelName;
        this.number = number;
        this.approvalState = approvalState;
    }
}
