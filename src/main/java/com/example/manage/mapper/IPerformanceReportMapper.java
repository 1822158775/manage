package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.PerformanceReport;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-03-29 16:50:54
 * 业绩记录管理
 */

public interface IPerformanceReportMapper extends BaseMapper<PerformanceReport> {
    List<PerformanceReport> queryAll(Map map);
    Integer queryCount(Map map);
}
