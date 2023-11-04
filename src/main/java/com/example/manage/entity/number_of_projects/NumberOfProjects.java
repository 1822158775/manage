package com.example.manage.entity.number_of_projects;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @avthor 潘小章
 * @date 2023/4/26
 */
@Data
@ToString
public class NumberOfProjects implements Serializable {
    public Integer numberOfPeople;//项目设定人数
    public Integer guideNumberOfPeople;//项目设定引导人数
    public Integer numberPersonnel;//现在已有人数
    public Integer realQuantity;//实际数量
    public Integer businessNumber;//业务数量
    public Integer guideNumber;//引导数量
    public String rateOfFullCapacity;//实际数量
    public String guideRateOfFullCapacity;//服务实际数量
    public String businessRateOfFullCapacity;//业务实际数量
    public String name;//项目名称
    public Integer id;//项目id

    public NumberOfProjects() {
    }

    public NumberOfProjects(Integer numberOfPeople, Integer guideNumberOfPeople, Integer numberPersonnel, Integer realQuantity, Integer businessNumber, Integer guideNumber, String rateOfFullCapacity, String guideRateOfFullCapacity, String businessRateOfFullCapacity, String name, Integer id) {
        this.numberOfPeople = numberOfPeople;
        this.guideNumberOfPeople = guideNumberOfPeople;
        this.numberPersonnel = numberPersonnel;
        this.realQuantity = realQuantity;
        this.businessNumber = businessNumber;
        this.guideNumber = guideNumber;
        this.rateOfFullCapacity = rateOfFullCapacity;
        this.guideRateOfFullCapacity = guideRateOfFullCapacity;
        this.businessRateOfFullCapacity = businessRateOfFullCapacity;
        this.name = name;
        this.id = id;
    }
}
