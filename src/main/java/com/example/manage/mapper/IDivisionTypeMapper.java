package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.DivisionType;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-06-19 11:06:56
 * 部门类型关联部门管理表
 */

public interface IDivisionTypeMapper extends BaseMapper<DivisionType> {
    List<DivisionType> queryAll(Map map);
    Integer queryCount(Map map);
}
