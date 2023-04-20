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
public class DataStatisticsTodayWeek implements Serializable {
    public String nametype;//类型名称

    public String startTime;//开始时间
    public String endTime;//结束时间

    public Integer activation;//本周激活
    public Integer approved;//本周批核
    public Integer artificial;//本周转人工
    public Integer refuse;//本周拒绝
    public Integer all;//本周所有数据

    public Double activationGrowthRate;//上周激活上升率
    public Double approvedGrowthRate;//上周批核上升率
    public Double artificialGrowthRate;//上周转人工上升率
    public Double refuseGrowthRate;//上周拒绝上升率
    public Double allGrowthRate;//上周所有数据上升率
    public Double validGrowthRate;//上周有效的率
    public Double ratioGrowthRate;//上周对比率
}
