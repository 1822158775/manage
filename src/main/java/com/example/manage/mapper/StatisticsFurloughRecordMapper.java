package com.example.manage.mapper;


import com.example.manage.entity.SysPersonnel;

import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/7/17
 */

public interface StatisticsFurloughRecordMapper {
    List<SysPersonnel> queryAll(Map map);
    Integer queryCount(Map map);
}
