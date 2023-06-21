package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.DivisionTypeManagement;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-06-19 11:02:17
 * 部门类型关联部门管理表
 */

public interface IDivisionTypeManagementMapper extends BaseMapper<DivisionTypeManagement> {
    List<DivisionTypeManagement> queryAll(Map map);
    Integer queryCount(Map map);
}
