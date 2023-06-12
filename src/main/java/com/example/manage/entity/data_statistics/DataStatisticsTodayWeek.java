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

    public Integer activation;//本周批核已激活
    public Integer approved;//本周批核未激活
    public Integer artificial;//本周转人工
    public Integer refuse;//本周拒绝
    public Integer all;//本周所有数据

    public String activationGrowthRate;//上周批核已激活上升率
    public String approvedGrowthRate;//上周批核未激活上升率
    public String artificialGrowthRate;//上周转人工上升率
    public String refuseGrowthRate;//上周拒绝上升率
    public String allGrowthRate;//上周所有数据上升率
    public String validGrowthRate;//上周有效的率
    public String ratioGrowthRate;//上周对比率
}
