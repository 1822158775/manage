package com.example.manage.entity.is_not_null;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

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
}
