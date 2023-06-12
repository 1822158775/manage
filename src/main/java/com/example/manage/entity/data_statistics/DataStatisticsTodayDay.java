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

    public Integer activation;//本日批核已激活
    public Integer approved;//本日批核未激活
    public Integer artificial;//本日转人工
    public Integer refuse;//本日拒绝
    public Integer all;//本日所有数据

    public String activationGrowthRate;//本日批核已激活上升率
    public String approvedGrowthRate;//本日批核未激活上升率
    public String artificialGrowthRate;//本日转人工上升率
    public String refuseGrowthRate;//本日拒绝上升率
    public String allGrowthRate;//本日所有数据上升率
    public String validGrowthRate;//本日有效的率
    public String ratioGrowthRate;//本日对比率

    public DataStatisticsTodayDay() {
    }

    public DataStatisticsTodayDay(Integer activation, Integer approved, Integer artificial) {
        this.activation = activation;
        this.approved = approved;
        this.artificial = artificial;
    }
}
