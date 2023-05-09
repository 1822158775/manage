package com.example.manage.entity.is_not_null;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @avthor 潘小章
 * @date 2023/3/23
 * 辅助实体类不能为空
 * null：可为空
 * isNotNull: 不可为空
 * isNotNullAndIsLengthNot0
 */
@Data
@ToString
public class SysManagementNotNull implements Serializable {
    public String id;//数据编码
    public String name;//名称
    public String projectAddress;//项目地址
    public String availableBalance;//可用余额
    public String monthlyIndicators;//每月指标
    public String managementState;//项目运行状态
    public String numberOfPeople;//人的数量

    public String southLatitude;//东南角经纬度
    public String northernLatitude;//西南角经纬度
    public String eastLongitude;//东北角经纬度
    public String westLongitude;//西北角经纬度

    public String startPunchIn;//上班打卡时间开始时间

    public String startNoonBreakTime;//午休开始时间
    public String endNoonBreakTime;//午休结束时间

    public String startClockOut;//下班打卡时间开始时间
    public String endClockOut;//下班打卡时间结束时间

    public SysManagementNotNull() {
    }

    public SysManagementNotNull(String id) {
        this.id = id;
    }

    public SysManagementNotNull(String name, String projectAddress, String monthlyIndicators, String managementState) {
        this.name = name;
        this.projectAddress = projectAddress;
        this.monthlyIndicators = monthlyIndicators;
        this.managementState = managementState;
    }

    public SysManagementNotNull(String id, String name, String projectAddress, String availableBalance, String monthlyIndicators, String managementState) {
        this.id = id;
        this.name = name;
        this.projectAddress = projectAddress;
        this.availableBalance = availableBalance;
        this.monthlyIndicators = monthlyIndicators;
        this.managementState = managementState;
    }

    public SysManagementNotNull(String name, String projectAddress, String monthlyIndicators, String managementState, String numberOfPeople) {
        this.name = name;
        this.projectAddress = projectAddress;
        this.monthlyIndicators = monthlyIndicators;
        this.managementState = managementState;
        this.numberOfPeople = numberOfPeople;
    }

    public SysManagementNotNull(String name, String projectAddress, String monthlyIndicators, String managementState, String numberOfPeople, String southLatitude, String northernLatitude, String eastLongitude, String westLongitude, String startPunchIn, String startClockOut, String endClockOut) {
        this.name = name;
        this.projectAddress = projectAddress;
        this.monthlyIndicators = monthlyIndicators;
        this.managementState = managementState;
        this.numberOfPeople = numberOfPeople;
        this.southLatitude = southLatitude;
        this.northernLatitude = northernLatitude;
        this.eastLongitude = eastLongitude;
        this.westLongitude = westLongitude;
        this.startPunchIn = startPunchIn;
        this.startClockOut = startClockOut;
        this.endClockOut = endClockOut;
    }
}
