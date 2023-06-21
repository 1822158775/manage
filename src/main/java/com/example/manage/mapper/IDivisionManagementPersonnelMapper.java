package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.DivisionManagementPersonnel;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-06-19 17:04:30
 * 部门类型关联部门管理表
 */

public interface IDivisionManagementPersonnelMapper extends BaseMapper<DivisionManagementPersonnel> {
    List<DivisionManagementPersonnel> queryAll(Map map);
    Integer queryCount(Map map);
}
