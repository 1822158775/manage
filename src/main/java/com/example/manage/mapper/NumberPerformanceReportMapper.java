package com.example.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.number.PerformanceReportNumber;

import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/4/3
 */

public interface NumberPerformanceReportMapper extends BaseMapper<PerformanceReportNumber> {
    PerformanceReportNumber queryOne(Map map);
}
