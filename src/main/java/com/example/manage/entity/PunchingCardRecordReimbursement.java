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

/**
 * @avthor 潘小章
 * @date 2024-08-09 15:11:58
 * 签到审核表
 */

@Data
@ToString
@TableName(value = "punching_card_record_reimbursement")
public class PunchingCardRecordReimbursement implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//
    @TableField(value = "personnel_id")
    public Integer personnelId;//人员数据编码
    @TableField(value = "verifier_remark")
    public String verifierRemark;//备注
    @TableField(value = "verifier_state")
    public String verifierState;//审核状态:pending,agree,refuse
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "verifier_time")
    public String verifierTime;//审核通过时间
    @TableField(value = "dispatch_code")
    public String dispatchCode;//编码
    @TableField(value = "dispatch_state")
    public String dispatchState;//审核标识
    @TableField(value = "number")
    public Integer number;//流转层级

    @TableField(exist = false)
    public String personnelName;

    public PunchingCardRecordReimbursement() {
    }

    public PunchingCardRecordReimbursement(Integer id, Integer personnelId, String verifierRemark, String verifierState, String verifierTime, String dispatchCode, String dispatchState, Integer number) {
        this.id = id;
        this.personnelId = personnelId;
        this.verifierRemark = verifierRemark;
        this.verifierState = verifierState;
        this.verifierTime = verifierTime;
        this.dispatchCode = dispatchCode;
        this.dispatchState = dispatchState;
        this.number = number;
    }
}
