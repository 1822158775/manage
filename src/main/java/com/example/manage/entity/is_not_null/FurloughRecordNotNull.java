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
 * @date 2023-05-17 11:15:58
 * 请假记录表
 */

@Data
@ToString
public class FurloughRecordNotNull implements Serializable {
    public String id;//
    public String personnelName;//请假申请人
    public String personnelId;//请假人唯一编码
    public String managementId;//请假项目组
    public String managementName;//请假项目组名称
    public String reissueState;//请假总状态
    public String maxNumber;//请假流转层级
    public String reissueCode;//请假编码
    public String reissueType;//请假类型
    public String startTime;//请假日期
    public String endTime;//请假日期
    public String applicantTime;//申请时间
    public String verifierRemark;//备注

    public FurloughRecordNotNull() {
    }

    public FurloughRecordNotNull(String id, String personnelName, String personnelId, String managementId, String managementName, String reissueState, String maxNumber, String reissueCode, String reissueType, String startTime, String endTime, String applicantTime, String verifierRemark) {
        this.id = id;
        this.personnelName = personnelName;
        this.personnelId = personnelId;
        this.managementId = managementId;
        this.managementName = managementName;
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
