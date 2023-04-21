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
 * @date 2023-03-29 11:16:50
 * 离职申请管理
 */

@Data
@ToString
@TableName(value = "manage_dimission")
public class ManageDimission implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "applicant")
    public String applicant;//申请人
    @TableField(value = "personnel_code")
    public String personnelCode;//申请人资源代码
    @TableField(value = "reasons_for_leaving")
    public String reasonsForLeaving;//离职原因
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "submission_time")
    public String submissionTime;//提交时间
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "resignation_time")
    public Date resignationTime;//离职时间
    @TableField(value = "applicant_state")
    public String applicantState;//审核状态
    @TableField(value = "management_id")
    public Integer managementId;//项目数据编码
    @TableField(value = "approver_personnel_id")
    public Integer approverPersonnelId;//审批人数据编码
    @TableField(value = "comments_from_reviewers")
    public String commentsFromReviewers;//审核人的留言
    @TableField(value = "approver_state")
    public String approverState;//状态
    @TableField(value = "report_coding")
    public String reportCoding;//报告编码
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "approver_time")
    public String approverTime;//审核时间
    @TableField(exist = false)
    public List<SysManagement> sysManagement;//项目信息
    @TableField(exist = false)
    public Integer personnelId;//申请人信息
    @TableField(exist = false)
    public SysPersonnel sysPersonnel;//申请人信息
    @TableField(exist = false)
    public SysPersonnel approverSysPersonnel;//审核人信息

    public ManageDimission() {
    }

    public ManageDimission(Integer id, String applicantState) {
        this.id = id;
        this.applicantState = applicantState;
    }
}
