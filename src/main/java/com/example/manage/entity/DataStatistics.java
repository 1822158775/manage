package com.example.manage.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @avthor 潘小章
 * @date 2023/4/18
 * 数据统计
 */
@Data
@ToString
public class DataStatistics implements Serializable {
    public Integer todayDayActivation;//本日激活
    public Integer todayDayApproved;//本日批核
    public Integer todayDayArtificial;//本日转人工
    public Integer todayDayRefuse;//本日拒绝
    public Integer todayDayAll;//本日所有数据

    public Integer todayWeekActivation;//本周激活
    public Integer todayWeekApproved;//本周批核
    public Integer todayWeekArtificial;//本周转人工
    public Integer todayWeekRefuse;//本周拒绝
    public Integer todayWeekAll;//本周所有数据

    public Integer todayMonthActivation;//本月激活
    public Integer todayMonthApproved;//本月批核
    public Integer todayMonthArtificial;//本月转人工
    public Integer todayMonthRefuse;//本月拒绝
    public Integer todayMonthAll;//本月所有数据

    public Integer todayYesterActivation;//昨日激活数据
    public Integer todayYesterApproved;//昨日批核
    public Integer todayYesterArtificial;//昨日转人工
    public Integer todayYesterRefuse;//昨日拒绝
    public Integer todayYesterAll;//昨日所有数据

    public Integer lastWeekActivation;//上周激活数据
    public Integer lastWeekApproved;//上周批核
    public Integer lastWeekArtificial;//上周转人工
    public Integer lastWeekRefuse;//上周拒绝
    public Integer lastWeekAll;//上周所有数据

    public Integer lastMonthActivation;//上月激活数据
    public Integer lastMonthApproved;//上月批核
    public Integer lastMonthArtificial;//上月转人工
    public Integer lastMonthRefuse;//上月拒绝
    public Integer lastMonthAll;//上月所有数据

    public Integer todayDayActivationGrowthRate;//本日激活上升率
    public Integer todayDayApprovedGrowthRate;//本日批核上升率
    public Integer todayDayArtificialGrowthRate;//本日转人工上升率
    public Integer todayDayRefuseGrowthRate;//本日拒绝上升率
    public Integer todayDayAllGrowthRate;//本日所有数据上升率

    public Integer todayWeekActivationGrowthRate;//上月激活上升率
    public Integer todayWeekApprovedGrowthRate;//上月批核上升率
    public Integer todayWeekArtificialGrowthRate;//上月转人工上升率
    public Integer todayWeekRefuseGrowthRate;//上月拒绝上升率
    public Integer todayWeekAllGrowthRate;//上月所有数据上升率

    public Integer todayMonthActivationGrowthRate;//上月激活数据上升率
    public Integer todayMonthApprovedGrowthRate;//上月批核上升率
    public Integer todayMonthArtificialGrowthRate;//上月转人工上升率
    public Integer todayMonthRefuseGrowthRate;//上月拒绝上升率
    public Integer todayMonthAllGrowthRate;//上月所有数据上升率

}
