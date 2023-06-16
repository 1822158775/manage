package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.PerformanceReportSales;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-06-16 11:43:54
 * 业绩关联权益
 */

public interface IPerformanceReportSalesMapper extends BaseMapper<PerformanceReportSales> {
    List<PerformanceReportSales> queryAll(Map map);
    Integer queryCount(Map map);
}
