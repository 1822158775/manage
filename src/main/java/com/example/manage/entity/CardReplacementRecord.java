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
 * @date 2023-05-16 09:47:02
 * 补卡
 */

@Data
@ToString
@TableName(value = "card_replacement_record")
public class CardReplacementRecord implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//
    @TableField(value = "personnel_name")
    public String personnelName;//补卡申请人
    @TableField(value = "personnel_id")
    public Integer personnelId;//补卡人唯一编码
    @TableField(value = "management_id")
    public Integer managementId;//补卡项目组
    @TableField(value = "management_name")
    public String managementName;//补卡项目组名称
    @TableField(value = "reissue_state")
    public String reissueState;//补卡总状态
    @TableField(value = "max_number")
    public Integer maxNumber;//补卡流转层级
    @TableField(value = "reissue_code")
    public String reissueCode;//补卡编码
    @TableField(value = "reissue_type")
    public String reissueType;//补卡类型
    @TableField(value = "verifier_remark")
    public String verifierRemark;//备注
    @TableField(value = "check_in_time_id")
    public Integer checkInTimeId;//打卡时间
    @TableField(value = "check_in_time_name")
    public String checkInTimeName;//打卡类型名称

    @DateTimeFormat(pattern ="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    @TableField(value = "reissue_time")
    public Date reissueTime;//补卡日期

    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "applicant_time")
    public Date applicantTime;//申请时间

    @TableField(exist = false)
    public List<CardReplacementReimbursement> replacementReimbursements;

    public CardReplacementRecord() {
    }

    public CardReplacementRecord(Integer id, Integer maxNumber) {
        this.id = id;
        this.maxNumber = maxNumber;
    }

    public CardReplacementRecord(Integer id, String personnelName, Integer personnelId, Integer managementId, String managementName, String reissueState, Integer maxNumber, String reissueCode, String reissueType, String verifierRemark, Date reissueTime, Date applicantTime) {
        this.id = id;
        this.personnelName = personnelName;
        this.personnelId = personnelId;
        this.managementId = managementId;
        this.managementName = managementName;
        this.reissueState = reissueState;
        this.maxNumber = maxNumber;
        this.reissueCode = reissueCode;
        this.reissueType = reissueType;
        this.verifierRemark = verifierRemark;
        this.reissueTime = reissueTime;
        this.applicantTime = applicantTime;
    }
}
