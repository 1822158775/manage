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
 * @date 2023-03-29 16:50:53
 * 业绩记录管理
 */

@Data
@ToString
@TableName(value = "performance_report")
public class PerformanceReport implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "personnel_code")
    public String personnelCode;//资源代码
    @TableField(value = "report_time")
    public String reportTime;//报告时间
    @TableField(value = "report_status")
    public String reportStatus;//拒绝，转人工，批核,激活
    @TableField(value = "management_id")
    public Integer managementId;//项目数据编码
    @TableField(value = "report_coding")
    public String reportCoding;//报告编码
    @TableField(value = "approver_personnel_id")
    public Integer approverPersonnelId;//审批人数据编码
    @TableField(value = "remark")
    public String remark;//报告备注
    @TableField(value = "comments_from_reviewers")
    public String commentsFromReviewers;//审核人的留言
    @TableField(value = "approver_state")
    public String approverState;//状态
    @TableField(value = "card_type_id")
    public Integer cardTypeId;//办卡类型id
    @TableField(exist = false)
    public SysPersonnel sysPersonnel;//申请人信息
    @TableField(exist = false)
    public Integer personnelId;//申请人信息
    @TableField(exist = false)
    public SysManagement sysManagement;//申请人所属项目信息
    @TableField(exist = false)
    public SysPersonnel approverSysPersonnel;//审核人信息
    @TableField(exist = false)
    public CardType cardType;//卡种信息
}
