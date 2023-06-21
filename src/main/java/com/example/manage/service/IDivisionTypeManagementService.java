package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-06-19 11:02:18
 * 部门类型关联部门管理表
 */

public interface IDivisionTypeManagementService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
