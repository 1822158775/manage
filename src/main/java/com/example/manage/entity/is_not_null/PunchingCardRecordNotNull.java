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
 * @date 2023-05-05 15:41:39
 * 打卡记录表
 */

@Data
@ToString
public class PunchingCardRecordNotNull implements Serializable {
    public String id;//数据编码
    public String name;//打卡人
    public String personnelCode;//资源代码
    public String clockInTime;//打卡时间
    public String managementId;//打卡项目
    public String workingAgoOpenId;//当前openid
    public String workingLaterOpenId;//打卡openid
    public String workingClockInState;//打卡状态，正常打卡，出差打卡，迟到，缺勤
    public String workingAttendanceTime;//上班打卡时间
    public String closedAgoOpenId;//当前openid
    public String closedLaterOpenId;//打卡openid
    public String closedClockInState;//打卡状态，正常打卡，出差打卡，早退，缺勤
    public String closedAttendanceTime;//下班打卡时间
    public String clockingDayTime;//打卡日期
    public String personnelId;//用户编码
    public String openId;//当前打卡的openid
    public String x;//x
    public String y;//y

    public PunchingCardRecordNotNull() {
    }

    public PunchingCardRecordNotNull(String id, String name, String personnelCode, String clockInTime, String managementId, String workingAgoOpenId, String workingLaterOpenId, String workingClockInState, String workingAttendanceTime, String closedAgoOpenId, String closedLaterOpenId, String closedClockInState, String closedAttendanceTime, String clockingDayTime, String personnelId, String openId, String x, String y) {
        this.id = id;
        this.name = name;
        this.personnelCode = personnelCode;
        this.clockInTime = clockInTime;
        this.managementId = managementId;
        this.workingAgoOpenId = workingAgoOpenId;
        this.workingLaterOpenId = workingLaterOpenId;
        this.workingClockInState = workingClockInState;
        this.workingAttendanceTime = workingAttendanceTime;
        this.closedAgoOpenId = closedAgoOpenId;
        this.closedLaterOpenId = closedLaterOpenId;
        this.closedClockInState = closedClockInState;
        this.closedAttendanceTime = closedAttendanceTime;
        this.clockingDayTime = clockingDayTime;
        this.personnelId = personnelId;
        this.openId = openId;
        this.x = x;
        this.y = y;
    }
}
