package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.DivisionManagement;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-06-19 11:30:35
 * 部门管理表
 */

public interface IDivisionManagementMapper extends BaseMapper<DivisionManagement> {
    List<DivisionManagement> queryAll(Map map);
    Integer queryCount(Map map);
}
