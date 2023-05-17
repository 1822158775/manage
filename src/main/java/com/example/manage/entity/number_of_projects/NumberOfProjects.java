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
    public Integer numberPersonnel;//现在已有人数
    public Integer realQuantity;//实际数量
    public Double rateOfFullCapacity;//实际数量
    public String name;//项目名称
    public Integer id;//项目id
}
