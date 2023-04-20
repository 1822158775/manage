package com.example.manage.mapper;

import com.example.manage.entity.data_statistics.DataStatistics;

import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/4/18
 */

public interface WhiteDataStatisticsMapper {
    DataStatistics queryAll(Map map);
}
