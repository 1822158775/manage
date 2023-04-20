package com.example.manage.entity.data_statistics;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @avthor 潘小章
 * @date 2023/4/19
 */

@Data
@ToString
public class DataStatisticsTodayMonth implements Serializable {
    public String nametype;//类型名称

    public String startTime;//开始时间
    public String endTime;//结束时间

    public Integer activation;//本月激活
    public Integer approved;//本月批核
    public Integer artificial;//本月转人工
    public Integer refuse;//本月拒绝
    public Integer all;//本月所有数据

    public Double activationGrowthRate;//上月激活数据上升率
    public Double approvedGrowthRate;//上月批核上升率
    public Double artificialGrowthRate;//上月转人工上升率
    public Double refuseGrowthRate;//上月拒绝上升率
    public Double allGrowthRate;//上月所有数据上升率
    public Double validGrowthRate;//上月有效的率
    public Double ratioGrowthRate;//上月对比率
}
