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

/**
 * @avthor 潘小章
 * @date 2023-05-10 17:56:11
 * 打卡时间表
 */

@Data
@ToString
@TableName(value = "check_in_time")
public class CheckInTime implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "name")
    public String name;//打卡类型

    @DateTimeFormat(pattern ="HH:mm:ss")
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
    @TableField(value = "start_punch_in")
    public String startPunchIn;//上班打卡时间开始时间
    @DateTimeFormat(pattern ="HH:mm:ss")
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
    @TableField(value = "end_punch_in")
    public String endPunchIn;//上班打卡时间结束时间

    @DateTimeFormat(pattern ="HH:mm:ss")
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
    @TableField(value = "start_noon_break_time")
    public String startNoonBreakTime;//午休开始时间
    @DateTimeFormat(pattern ="HH:mm:ss")
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
    @TableField(value = "end_noon_break_time")
    public String endNoonBreakTime;//午休结束时间

    @DateTimeFormat(pattern ="HH:mm:ss")
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
    @TableField(value = "start_clock_out")
    public String startClockOut;//下班打卡时间开始时间
    @DateTimeFormat(pattern ="HH:mm:ss")
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
    @TableField(value = "end_clock_out")
    public String endClockOut;//下班打卡时间结束时间

    @TableField(value = "management_id")
    public Integer managementId;//项目编码
}
