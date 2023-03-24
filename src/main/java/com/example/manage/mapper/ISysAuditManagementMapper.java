package com.example.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.SysAuditManagement;

import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/3/24
 */

public interface ISysAuditManagementMapper extends BaseMapper<SysAuditManagement> {
    List<SysAuditManagement> queryAll(Map map);
    Integer queryCount(Map map);
}
