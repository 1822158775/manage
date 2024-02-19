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
 * @date 2023-06-26 15:52:39
 * 出差记录表
 */

@Data
@ToString
public class GoOutToWorkNotNull implements Serializable {
    public String id;//数据编码
    public String personnelName;//外出办公申请人
    public String personnelId;//请假人唯一编码
    public String reissueState;//请假总状态
    public String maxNumber;//请假流转层级
    public String reissueCode;//请假编码
    public String reissueType;//请假类型
    public String startTime;//请假开始日期
    public String applicantTime;//申请时间
    public String verifierRemark;//备注
    public String endTime;//请假结束时间


    public String setOut;/*出发地*/
    public String traffic;/*交通方式*/
    public String reach;/*到达*/
    public String estimatedCost;/*预计费用*/
    public String onBusiness;/*出差办事*/
    public GoOutToWorkNotNull() {
    }

    public GoOutToWorkNotNull(String id, String personnelName, String personnelId, String reissueState, String maxNumber, String reissueCode, String reissueType, String startTime, String applicantTime, String verifierRemark, String endTime) {
        this.id = id;
        this.personnelName = personnelName;
        this.personnelId = personnelId;
        this.reissueState = reissueState;
        this.maxNumber = maxNumber;
        this.reissueCode = reissueCode;
        this.reissueType = reissueType;
        this.startTime = startTime;
        this.applicantTime = applicantTime;
        this.verifierRemark = verifierRemark;
        this.endTime = endTime;
    }

    public GoOutToWorkNotNull(String id, String personnelName, String personnelId, String reissueState, String maxNumber, String reissueCode, String reissueType, String startTime, String applicantTime, String verifierRemark, String endTime, String setOut, String traffic, String reach, String estimatedCost, String onBusiness) {
        this.id = id;
        this.personnelName = personnelName;
        this.personnelId = personnelId;
        this.reissueState = reissueState;
        this.maxNumber = maxNumber;
        this.reissueCode = reissueCode;
        this.reissueType = reissueType;
        this.startTime = startTime;
        this.applicantTime = applicantTime;
        this.verifierRemark = verifierRemark;
        this.endTime = endTime;
        this.setOut = setOut;
        this.traffic = traffic;
        this.reach = reach;
        this.estimatedCost = estimatedCost;
        this.onBusiness = onBusiness;
    }
}
