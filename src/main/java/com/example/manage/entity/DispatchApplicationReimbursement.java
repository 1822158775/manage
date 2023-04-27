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
 * @date 2023-04-26 11:17:41
 * 调派关联审批人
 */

@Data
@ToString
@TableName(value = "dispatch_application_reimbursement")
public class DispatchApplicationReimbursement implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "personnel_id")
    public Integer personnelId;//人员数据编码
    @TableField(value = "verifier_remark")
    public String verifierRemark;//备注
    @TableField(value = "verifier_state")
    public String verifierState;//审核状态
    @TableField(value = "verifier_time")
    public String verifierTime;//审核通过时间
    @TableField(value = "dispatch_code")
    public String dispatchCode;//调派编码
    @TableField(value = "dispatch_state")
    public String dispatchState;//调派审核标识
    @TableField(value = "number")
    public Integer number;//流转层级
    @TableField(exist = false)
    public String personnelName;//人员名称
    public DispatchApplicationReimbursement() {
    }

    public DispatchApplicationReimbursement(Integer id, Integer personnelId, String verifierRemark, String verifierState, String verifierTime, String dispatchCode, String dispatchState, Integer number) {
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
