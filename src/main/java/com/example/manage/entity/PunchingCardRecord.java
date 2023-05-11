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
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

/**
 * @avthor 潘小章
 * @date 2023-05-05 15:41:39
 * 打卡记录表
 */

@Data
@ToString
@TableName(value = "punching_card_record")
public class PunchingCardRecord implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "name")
    public String name;//打卡人
    @TableField(value = "personnel_code")
    public String personnelCode;//资源代码
    @TableField(value = "clock_in_time")
    public String clockInTime;//打卡时间
    @TableField(value = "management_id")
    public Integer managementId;//打卡项目

    @TableField(value = "working_ago_open_id")
    public String workingAgoOpenId;//当前openid
    @TableField(value = "working_later_open_id")
    public String workingLaterOpenId;//打卡openid
    @TableField(value = "working_clock_in_state")
    public String workingClockInState;//打卡状态，正常打卡，出差打卡，迟到，缺勤

    @DateTimeFormat(pattern ="HH:mm:ss")
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
    @TableField(value = "working_attendance_time")
    public String workingAttendanceTime;//上班打卡时间

    @TableField(value = "closed_ago_open_id")
    public String closedAgoOpenId;//当前openid
    @TableField(value = "closed_later_open_id")
    public String closedLaterOpenId;//打卡openid
    @TableField(value = "closed_clock_in_state")
    public String closedClockInState;//打卡状态，正常打卡，出差打卡，早退，缺勤

    @DateTimeFormat(pattern ="HH:mm:ss")
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
    @TableField(value = "closed_attendance_time")
    public String closedAttendanceTime;//下班打卡时间

    @DateTimeFormat(pattern ="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    @TableField(value = "clocking_day_time")
    public String clockingDayTime;//打卡日期

    @DateTimeFormat(pattern ="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    @TableField(value = "management_start_time")
    public String managementStartTime;//记录打卡上班时间

    @DateTimeFormat(pattern ="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    @TableField(value = "management_end_time")
    public String managementEndTime;//记录打卡下班时间

    @TableField(value = "check_in_time_id")
    public Integer checkInTimeId;//打卡时间
    @TableField(value = "check_in_time_name")
    public String checkInTimeName;//打卡项目

    @TableField(exist = false)
    public String supervisor;//主管名称
    @TableField(exist = false)
    public Integer employmentStatus;//任职状态(1：在职，0：离职,2:待入职)
    @TableField(exist = false)
    public Integer personnelId;//用户编码
    @TableField(exist = false)
    public String openId;//当前打卡的openid
    @TableField(exist = false)
    public Double x;//x
    @TableField(exist = false)
    public Double y;//y

    @TableField(exist = false)
    public List<SysManagement> sysManagement;//项目数据

    @TableField(exist = false)
    public SysPersonnel sysPersonnel;//人员信息

    @TableField(exist = false)
    public SysManagement management;//项目信息

    @TableField(exist = false)
    public String checkInId;//打卡类型

    @TableField(exist = false)
    public List<CheckInTime> checkInTimes;

    public PunchingCardRecord() {
    }

    public PunchingCardRecord(Integer id, String name, String personnelCode, String clockInTime, Integer managementId, String workingAgoOpenId, String workingLaterOpenId, String workingClockInState, String workingAttendanceTime, String closedAgoOpenId, String closedLaterOpenId, String closedClockInState, String closedAttendanceTime, String clockingDayTime, String managementStartTime, String managementEndTime,Integer checkInTimeId,String checkInTimeName) {
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
        this.managementStartTime = managementStartTime;
        this.managementEndTime = managementEndTime;
        this.checkInTimeId = checkInTimeId;
        this.checkInTimeName = checkInTimeName;
    }
}
