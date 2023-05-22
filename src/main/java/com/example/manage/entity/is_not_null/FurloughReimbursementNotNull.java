package com.example.manage.entity.is_not_null;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;

/**
 * @avthor 潘小章
 * @date 2023-05-17 11:45:41
 * 请假审核表
 */

@Data
@ToString
public class FurloughReimbursementNotNull implements Serializable {
    public String id;//数据编码
    public String personnelId;//人员数据编码
    public String verifierRemark;//备注
    public String verifierState;//审核状态:pending,agree,refuse
    public String verifierTime;//审核通过时间
    public String reissueCode;//请假编码
    public String reissueState;//请假审核标识
    public String number;//流转层级

    public FurloughReimbursementNotNull() {
    }

    public FurloughReimbursementNotNull(String id, String personnelId, String verifierRemark, String verifierState, String verifierTime, String reissueCode, String reissueState, String number) {
        this.id = id;
        this.personnelId = personnelId;
        this.verifierRemark = verifierRemark;
        this.verifierState = verifierState;
        this.verifierTime = verifierTime;
        this.reissueCode = reissueCode;
        this.reissueState = reissueState;
        this.number = number;
    }
}
