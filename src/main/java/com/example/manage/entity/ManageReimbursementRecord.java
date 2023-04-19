package com.example.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

/**
 * @avthor 潘小章
 * @date 2023-04-11 15:00:10
 * 申请报销记录管理
 */

@Data
@ToString
@TableName(value = "manage_reimbursement_record")
public class ManageReimbursementRecord implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "phone")
    public String phone;//手机号
    @TableField(value = "declaration_code")
    public String declarationCode;//申报编码
    @TableField(value = "account")
    public String account;//申报账号
    @TableField(value = "applicant")
    public String applicant;//申请人
    @TableField(value = "reimbursement_type")
    public String reimbursementType;//报销类型:固定(gd),可变(kb)
    @TableField(value = "amount_declared")
    public Double amountDeclared;//申报总金额
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "declaration_time")
    public String declarationTime;//申报时间
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "approver_time")
    public String approverTime;//审核时间
    @TableField(value = "remark")
    public String remark;//备注
    @TableField(value = "approver_state")
    public String approverState;//状态审核状态:pending,agree,refuse
    @TableField(value = "bank_account")
    public String bankAccount;//银行账户
    @TableField(value = "bank_of_deposit")
    public String bankOfDeposit;//开户银行
    @TableField(value = "max_number")
    public Integer maxNumber;//最高的数字

    @TableField(exist = false)
    public List<ReimbursementApproval> reimbursementApprovals;//报销记录关联审批人进行审批
    @TableField(exist = false)
    public List<ReimbursementCategory> reimbursementCategories;//报销记录关联类目
    @TableField(exist = false)
    public List<ReimbursementCopy> reimbursementCopies;//报销记录抄送人
    @TableField(exist = false)
    public List<ReimbursementProject> reimbursementProjects;//报销记录关联项目

    @TableField(exist = false)
    public Integer[] approvalNumber;//报销记录关联审批人进行审批
    @TableField(exist = false)
    public Integer[] categoriesNumber;//报销记录关联类目
    @TableField(exist = false)
    public Integer[] copiesNumber;//报销记录抄送人
    @TableField(exist = false)
    public Integer[] projectsNumber;//报销记录关联项目

    @TableField(exist = false)
    public Integer personnelId;//人员编码

    public ManageReimbursementRecord() {
    }

    public ManageReimbursementRecord(Integer id, Integer maxNumber) {
        this.id = id;
        this.maxNumber = maxNumber;
    }

    public ManageReimbursementRecord(Integer id, String approverTime, String remark, String approverState) {
        this.id = id;
        this.approverTime = approverTime;
        this.remark = remark;
        this.approverState = approverState;
    }
}
