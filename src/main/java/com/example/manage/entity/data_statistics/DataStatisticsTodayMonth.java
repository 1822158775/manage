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

    public Integer activation;//本月批核已激活
    public Integer approved;//本月批核未激活
    public Integer artificial;//本月转人工
    public Integer refuse;//本月拒绝
    public Integer all;//本月所有数据

    public String activationGrowthRate;//上月批核已激活数据上升率
    public String approvedGrowthRate;//上月批核未激活上升率
    public String artificialGrowthRate;//上月转人工上升率
    public String refuseGrowthRate;//上月拒绝上升率
    public String allGrowthRate;//上月所有数据上升率
    public String validGrowthRate;//上月有效的率
    public String ratioGrowthRate;//上月对比率
}
