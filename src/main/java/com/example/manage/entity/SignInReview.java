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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * @avthor 潘小章
 * @date 2024-08-02 10:58:30
 * 视频签到申请表
 */

@Data
@ToString
@TableName(value = "sign_in_review")
public class SignInReview implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//
    @TableField(value = "name")
    public String name;//打卡人
    @TableField(value = "personnel_code")
    public String personnelCode;//资源代码
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "clock_in_time")
    public String clockInTime;//签到时间
    @TableField(value = "management_id")
    public Integer managementId;//签到项目
    @TableField(value = "ago_open_id")
    public String agoOpenId;//当前openid
    @TableField(value = "later_open_id")
    public String laterOpenId;//打卡openid
    @TableField(value = "clock_in_state")
    public String clockInState;//打卡状态，打卡成功，出差打卡，迟到，缺勤
    @DateTimeFormat(pattern ="HH:mm:ss")
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
    @TableField(value = "attendance_time")
    public String attendanceTime;//上班打卡时间
    @DateTimeFormat(pattern ="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    @TableField(value = "clocking_day_time")
    public String clockingDayTime;//打卡日期
    @TableField(value = "check_in_time_id")
    public Integer checkInTimeId;//打卡时间表的id
    @TableField(value = "check_in_time_name")
    public String checkInTimeName;//打卡类型名称
    @TableField(value = "ago_state")
    public String agoState;//打卡和登录时间间隔
    @TableField(value = "model")
    public String model;//上班打卡手机类型
    @TableField(value = "punching_card_record_code")
    public String punchingCardRecordCode;//打卡编码
    @TableField(value = "check_in_type")
    public String checkInType;//上班打卡类型
    @TableField(value = "check_remark")
    public String remark;//下班备注
    @TableField(value = "sign_in_type")
    public String signInType;//签到类型
    @TableField(value = "dispatch_code")
    public String dispatchCode;//唯一编码
    @TableField(value = "verifier_state")
    public String verifierState;//审核状态:pending,agree,refuse
    @TableField(value = "max_number")
    public Integer maxNumber;//层转流级

    @TableField(exist = false)
    public BigDecimal bigDecimal;//测试

    @TableField(exist = false)
    public String openId;//当前打卡的openid
    @TableField(exist = false)
    public Integer personnelId;//用户编码

    @TableField(exist = false)
    public SysManagement management;//项目信息

    @TableField(exist = false)
    public String checkInId;//打卡类型

    @TableField(exist = false)
    public String videoPath;//视频路径
    @TableField(exist = false)
    public List<ReimbursementImage> reimbursementImages;//附件

    public SignInReview() {
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal.setScale(4, RoundingMode.HALF_UP);
    }

    public SignInReview(Integer id, Integer maxNumber) {
        this.id = id;
        this.maxNumber = maxNumber;
    }

    public SignInReview(Integer id, String remark, String verifierState) {
        this.id = id;
        this.remark = remark;
        this.verifierState = verifierState;
    }

    public SignInReview(Integer id, String name, String personnelCode, String clockInTime, Integer managementId, String agoOpenId, String laterOpenId, String clockInState, String attendanceTime, String clockingDayTime, Integer checkInTimeId, String checkInTimeName, String agoState, String model, String punchingCardRecordCode, String checkInType, String remark, String signInType, String dispatchCode, String verifierState) {
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
        this.remark = remark;
        this.signInType = signInType;
        this.dispatchCode = dispatchCode;
        this.verifierState = verifierState;
    }
}
