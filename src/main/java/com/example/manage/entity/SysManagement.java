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
 * @date 2023/3/23
 * 项目实体类
 */
@Data
@ToString
@TableName(value = "sys_management")
public class SysManagement implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "name")
    public String name;//名称
    @TableField(value = "project_address")
    public String projectAddress;//项目地址
    @TableField(value = "available_balance")
    public Double availableBalance;//可用余额
    @TableField(value = "monthly_indicators")
    public Integer monthlyIndicators;//每月指标
    @TableField(value = "management_state")
    public Integer managementState;//项目运行状态
    @TableField(value = "management_code")
    public String managementCode;//项目编码
    @TableField(value = "number_of_people")
    public Integer numberOfPeople;//人的数量
    @TableField(value = "guide_number_of_people")
    public Integer guideNumberOfPeople;//引导人的数量

    @TableField(value = "south_latitude")
    public String southLatitude;//东南角经纬度
    @TableField(value = "northern_latitude")
    public String northernLatitude;//西南角经纬度
    @TableField(value = "east_longitude")
    public String eastLongitude;//东北角经纬度
    @TableField(value = "west_longitude")
    public String westLongitude;//西北角经纬度


    @DateTimeFormat(pattern ="HH:mm:ss")
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
    @TableField(value = "start_punch_in")
    public String startPunchIn;//上班打卡时间开始时间

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


    @TableField(exist = false)
    public List<CardType> cardTypeS;//卡种管理
    @TableField(exist = false)
    public List<CardType> allCardType;//所有卡种管理
    @TableField(exist = false)
    public Integer[] integers;//卡种操作数组

    public SysManagement() {
    }

    public SysManagement(Integer id, Double availableBalance) {
        this.id = id;
        this.availableBalance = availableBalance;
    }

    public SysManagement(String name, String projectAddress, Double availableBalance, Integer monthlyIndicators, Integer managementState) {
        this.name = name;
        this.projectAddress = projectAddress;
        this.availableBalance = availableBalance;
        this.monthlyIndicators = monthlyIndicators;
        this.managementState = managementState;
    }
}
