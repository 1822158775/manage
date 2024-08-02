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
 * @date 2024-08-02 10:58:31
 * 视频签到申请表
 */

@Data
@ToString
public class SignInReviewNotNull implements Serializable {
    public String id;//
    public String name;//打卡人
    public String personnelCode;//资源代码
    public String clockInTime;//签到时间
    public String managementId;//签到项目
    public String agoOpenId;//当前openid
    public String laterOpenId;//打卡openid
    public String clockInState;//打卡状态，打卡成功，出差打卡，迟到，缺勤
    public String attendanceTime;//上班打卡时间
    public String clockingDayTime;//打卡日期
    public String checkInTimeId;//打卡时间表的id
    public String checkInTimeName;//打卡类型名称
    public String agoState;//打卡和登录时间间隔
    public String model;//上班打卡手机类型
    public String punchingCardRecordCode;//打卡编码
    public String checkInType;//上班打卡类型
    public String checkRemark;//下班备注
    public String signInType;//签到类型
    public String dispatchCode;//唯一编码
    public String verifierState;//审核状态:pending,agree,refuse
    public String videoPath;//视频路径
    public String personnelId;//用户编码

    public SignInReviewNotNull() {
    }

    public SignInReviewNotNull(String id, String name, String personnelCode, String clockInTime, String managementId, String agoOpenId, String laterOpenId, String clockInState, String attendanceTime, String clockingDayTime, String checkInTimeId, String checkInTimeName, String agoState, String model, String punchingCardRecordCode, String checkInType, String checkRemark, String signInType, String dispatchCode, String verifierState, String videoPath, String personnelId) {
        this.id = id;
        this.name = name;
        this.personnelCode = personnelCode;
        this.clockInTime = clockInTime;
        this.managementId = managementId;
        this.agoOpenId = agoOpenId;
        this.laterOpenId = laterOpenId;
        this.clockInState = clockInState;
        this.attendanceTime = attendanceTime;
        this.clockingDayTime = clockingDayTime;
        this.checkInTimeId = checkInTimeId;
        this.checkInTimeName = checkInTimeName;
        this.agoState = agoState;
        this.model = model;
        this.punchingCardRecordCode = punchingCardRecordCode;
        this.checkInType = checkInType;
        this.checkRemark = checkRemark;
        this.signInType = signInType;
        this.dispatchCode = dispatchCode;
        this.verifierState = verifierState;
        this.videoPath = videoPath;
        this.personnelId = personnelId;
    }
}
