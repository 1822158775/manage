package com.example.manage.mapper;

import com.example.manage.entity.SysManagement;

import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/4/26
 */

public interface WhiteSysManagementMapper {
    List<SysManagement> queryAll(Map map);
}
