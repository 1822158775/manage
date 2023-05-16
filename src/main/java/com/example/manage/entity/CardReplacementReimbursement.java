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
 * @date 2023-05-16 10:30:12
 * 补卡审核人
 */

@Data
@ToString
@TableName(value = "card_replacement_reimbursement")
public class CardReplacementReimbursement implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "personnel_id")
    public Integer personnelId;//人员数据编码
    @TableField(value = "verifier_remark")
    public String verifierRemark;//备注
    @TableField(value = "verifier_state")
    public String verifierState;//审核状态:pending,agree,refuse
    @TableField(value = "verifier_time")
    public String verifierTime;//审核通过时间
    @TableField(value = "reissue_code")
    public String reissueCode;//补卡编码
    @TableField(value = "reissue_state")
    public String reissueState;//补卡审核标识
    @TableField(value = "number")
    public Integer number;//流转层级

    public CardReplacementReimbursement() {
    }

    public CardReplacementReimbursement(Integer id, Integer personnelId, String verifierRemark, String verifierState) {
        this.id = id;
        this.personnelId = personnelId;
        this.verifierRemark = verifierRemark;
        this.verifierState = verifierState;
    }
}
