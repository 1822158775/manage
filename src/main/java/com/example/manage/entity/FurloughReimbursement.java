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
 * @date 2023-05-17 11:45:41
 * 请假审核表
 */

@Data
@ToString
@TableName(value = "furlough_reimbursement")
public class FurloughReimbursement implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "personnel_id")
    public Integer personnelId;//人员数据编码
    @TableField(value = "verifier_remark")
    public String verifierRemark;//备注
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "verifier_time")
    public String verifierTime;//审核通过时间
    @TableField(value = "reissue_code")
    public String reissueCode;//请假编码
    @TableField(value = "reissue_state")
    public String reissueState;//请假审核标识
    @TableField(value = "number")
    public Integer number;//流转层级

    @TableField(exist = false)
    public String personnelName;

    public FurloughReimbursement() {
    }

    public FurloughReimbursement(Integer id, Integer personnelId, String verifierRemark, String reissueState) {
        this.id = id;
        this.personnelId = personnelId;
        this.verifierRemark = verifierRemark;
        this.reissueState = reissueState;
    }

    public FurloughReimbursement(Integer id, Integer personnelId, String verifierRemark, String verifierTime, String reissueCode, String reissueState, Integer number) {
        this.id = id;
        this.personnelId = personnelId;
        this.verifierRemark = verifierRemark;
        this.verifierTime = verifierTime;
        this.reissueCode = reissueCode;
        this.reissueState = reissueState;
        this.number = number;
    }
}
