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
 * @date 2023-05-16 09:47:03
 * 补卡
 */

@Data
@ToString
public class CardReplacementRecordNotNull implements Serializable {
    public String id;//
    public String personnelName;//补卡申请人
    public String personnelId;//补卡人唯一编码
    public String managementId;//补卡项目组
    public String managementName;//补卡项目组名称
    public String reissueState;//补卡总状态
    public String maxNumber;//补卡流转层级
    public String reissueCode;//补卡编码
    public String reissueType;//补卡类型
    public String reissueTime;//补卡日期
    public String checkInTimeId;//打卡时间
    public String checkInTimeName;//打卡类型名称

    public CardReplacementRecordNotNull() {
    }

    public CardReplacementRecordNotNull(String id, String personnelName, String personnelId, String managementId, String managementName, String reissueState, String maxNumber, String reissueCode, String reissueType, String reissueTime, String checkInTimeId, String checkInTimeName) {
        this.id = id;
        this.personnelName = personnelName;
        this.personnelId = personnelId;
        this.managementId = managementId;
        this.managementName = managementName;
        this.reissueState = reissueState;
        this.maxNumber = maxNumber;
        this.reissueCode = reissueCode;
        this.reissueType = reissueType;
        this.reissueTime = reissueTime;
        this.checkInTimeId = checkInTimeId;
        this.checkInTimeName = checkInTimeName;
    }
}
