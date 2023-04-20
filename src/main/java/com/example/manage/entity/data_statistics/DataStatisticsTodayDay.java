package com.example.manage.entity.data_statistics;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @avthor 潘小章
 * @date 2023/4/19
 * 本日
 */
@Data
@ToString
public class DataStatisticsTodayDay implements Serializable {
    public String nametype;//类型名称

    public String startTime;//开始时间
    public String endTime;//结束时间

    public Integer activation;//本日激活
    public Integer approved;//本日批核
    public Integer artificial;//本日转人工
    public Integer refuse;//本日拒绝
    public Integer all;//本日所有数据

    public Double activationGrowthRate;//本日激活上升率
    public Double approvedGrowthRate;//本日批核上升率
    public Double artificialGrowthRate;//本日转人工上升率
    public Double refuseGrowthRate;//本日拒绝上升率
    public Double allGrowthRate;//本日所有数据上升率
    public Double validGrowthRate;//本日有效的率
    public Double ratioGrowthRate;//本日对比率


}
