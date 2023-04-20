package com.example.manage.entity.data_statistics;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @avthor 潘小章
 * @date 2023/4/18
 * 数据统计
 */
@Data
@ToString
public class DataStatistics implements Serializable {
    public List<DataStatisticsTodayDay> dataStatisticsTodayDays;//本日
    public List<DataStatisticsTodayWeek> dataStatisticsTodayWeeks;//本周
    public List<DataStatisticsTodayMonth> dataStatisticsTodayMonths;//本月s
}
