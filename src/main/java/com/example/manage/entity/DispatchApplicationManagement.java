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
import java.util.Date;
import java.util.List;

/**
 * @avthor 潘小章
 * @date 2023-03-29 11:28:22
 * 调派管理
 */

@Data
@ToString
@TableName(value = "dispatch_application_management")
public class DispatchApplicationManagement implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "applicant")
    public String applicant;//申请人
    @TableField(value = "phone")
    public String phone;//手机号
    @TableField(value = "personnel_code")
    public String personnelCode;//申请人资源代码
    @TableField(value = "remark")
    public String remark;//调派原因
    @TableField(value = "ago_management_id")
    public Integer agoManagementId;//调派前项目数据编码
    @TableField(value = "later_management_id")
    public Integer laterManagementId;//调派后项目数据编码
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "ago_audit_time")
    public String agoAuditTime;//调派前审核时间
    @TableField(value = "ago_personnel_id")
    public Integer agoPersonnelId;//调派前审核人的数据编码
    @TableField(value = "ago_verifier_remark")
    public String agoVerifierRemark;//调派前审核人留言
    @TableField(value = "ago_verifier_state")
    public String agoVerifierState;//调派前审核人状态
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "later_audit_time")
    public String laterAuditTime;//调派后审核时间
    @TableField(value = "later_personnel_id")
    public Integer laterPersonnelId;//调派后审核人
    @TableField(value = "later_verifier_remark")
    public String laterVerifierRemark;//调派后审核人留言
    @TableField(value = "later_verifier_state")
    public String laterVerifierState;//调派后审核人状态

    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "dispathch_time")
    public Date dispathchTime;//调派时间

    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "applicant_time")
    public Date applicantTime;//申请时间

    @TableField(value = "dispatch_code")
    public String dispatchCode;//调派代码
    @TableField(value = "max_number")
    public Integer maxNumber;//审核流转层级
    @TableField(value = "applicant_state")
    public String applicantState;//审核总状态:pending,agree,refuse

    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "verifier_time")
    public Date verifierTime;//审核通过时间

    @TableField(exist = false)
    public SysPersonnel verifierSysPersonnel;//当前项目审批人
    @TableField(exist = false)
    public SysManagement agoVerifierSysManagement;//当前项目
    @TableField(exist = false)
    public SysManagement laterVerifierSysManagement;//调派后项目
    @TableField(exist = false)
    public Integer personnelId;//申请人资源代码
    @TableField(exist = false)
    public String verifierRemark;//审核人留言
    @TableField(exist = false)
    public String verifierState;//审核人状态
    @TableField(exist = false)
    public List<SysPersonnel> sysPersonnels;//人员信息
    @TableField(exist = false)
    public List<DispatchApplicationReimbursement> dispatchApplicationReimbursements;//审核人列表

    public DispatchApplicationManagement() {
    }

    public DispatchApplicationManagement(Integer id, Integer maxNumber) {
        this.id = id;
        this.maxNumber = maxNumber;
    }

    public DispatchApplicationManagement(Integer id, String verifierRemark, String verifierState) {
        this.id = id;
        this.verifierRemark = verifierRemark;
        this.verifierState = verifierState;
    }

    public DispatchApplicationManagement(Integer id, String applicant, String phone, String personnelCode, String remark, Integer agoManagementId, Integer laterManagementId, String agoAuditTime, Integer agoPersonnelId, String agoVerifierRemark, String agoVerifierState, String laterAuditTime, Integer laterPersonnelId, String laterVerifierRemark, String laterVerifierState, Date dispathchTime, Date applicantTime, String dispatchCode, Integer maxNumber, String applicantState, Date verifierTime) {
        this.id = id;
        this.applicant = applicant;
        this.phone = phone;
        this.personnelCode = personnelCode;
        this.remark = remark;
        this.agoManagementId = agoManagementId;
        this.laterManagementId = laterManagementId;
        this.agoAuditTime = agoAuditTime;
        this.agoPersonnelId = agoPersonnelId;
        this.agoVerifierRemark = agoVerifierRemark;
        this.agoVerifierState = agoVerifierState;
        this.laterAuditTime = laterAuditTime;
        this.laterPersonnelId = laterPersonnelId;
        this.laterVerifierRemark = laterVerifierRemark;
        this.laterVerifierState = laterVerifierState;
        this.dispathchTime = dispathchTime;
        this.applicantTime = applicantTime;
        this.dispatchCode = dispatchCode;
        this.maxNumber = maxNumber;
        this.applicantState = applicantState;
        this.verifierTime = verifierTime;
    }

    public DispatchApplicationManagement(Integer id, String agoAuditTime, Integer agoPersonnelId, String agoVerifierRemark, String agoVerifierState, String laterAuditTime, Integer laterPersonnelId, String laterVerifierRemark, String laterVerifierState) {
        this.id = id;
        this.agoAuditTime = agoAuditTime;
        this.agoPersonnelId = agoPersonnelId;
        this.agoVerifierRemark = agoVerifierRemark;
        this.agoVerifierState = agoVerifierState;
        this.laterAuditTime = laterAuditTime;
        this.laterPersonnelId = laterPersonnelId;
        this.laterVerifierRemark = laterVerifierRemark;
        this.laterVerifierState = laterVerifierState;
    }
}
