package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.SysTableName;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-03-24 14:04:29
 * 数据表名称管理
 */

public interface ISysTableNameMapper extends BaseMapper<SysTableName> {
    List<SysTableName> queryAll(Map map);
    Integer queryCount(Map map);
}
