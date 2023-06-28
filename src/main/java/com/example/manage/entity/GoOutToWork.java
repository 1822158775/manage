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
 * @date 2023-06-26 15:52:39
 * 出差记录表
 */

@Data
@ToString
@TableName(value = "go_out_to_work")
public class GoOutToWork implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "personnel_name")
    public String personnelName;//外出办公申请人
    @TableField(value = "personnel_id")
    public Integer personnelId;//请假人唯一编码
    @TableField(value = "reissue_state")
    public String reissueState;//请假总状态
    @TableField(value = "max_number")
    public Integer maxNumber;//请假流转层级
    @TableField(value = "reissue_code")
    public String reissueCode;//请假编码
    @TableField(value = "reissue_type")
    public String reissueType;//请假类型

    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "start_time")
    public Date startTime;//请假开始日期

    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "applicant_time")
    public Date applicantTime;//申请时间

    @TableField(value = "verifier_remark")
    public String verifierRemark;//备注

    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "end_time")
    public Date endTime;//请假结束时间

    @TableField(exist = false)
    public List<ReimbursementImage> reimbursementImages;//附件

    @TableField(exist = false)
    public List<GoOutToWorkReimbursement> goOutToWorkReimbursements;//审核人流程

    public GoOutToWork() {
    }

    public GoOutToWork(Integer id, Integer maxNumber) {
        this.id = id;
        this.maxNumber = maxNumber;
    }

    public GoOutToWork(Integer id, String personnelName, Integer personnelId, String reissueState, Integer maxNumber, String reissueCode, String reissueType, Date startTime, Date endTime, Date applicantTime, String verifierRemark) {
        this.id = id;
        this.personnelName = personnelName;
        this.personnelId = personnelId;
        this.reissueState = reissueState;
        this.maxNumber = maxNumber;
        this.reissueCode = reissueCode;
        this.reissueType = reissueType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.applicantTime = applicantTime;
        this.verifierRemark = verifierRemark;
    }
}
