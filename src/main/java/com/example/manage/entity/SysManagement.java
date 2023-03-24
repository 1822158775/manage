package com.example.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

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
    public Boolean managementState;//项目运行状态

    public SysManagement() {
    }

    public SysManagement(String name, String projectAddress, Double availableBalance, Integer monthlyIndicators, Boolean managementState) {
        this.name = name;
        this.projectAddress = projectAddress;
        this.availableBalance = availableBalance;
        this.monthlyIndicators = monthlyIndicators;
        this.managementState = managementState;
    }
}
