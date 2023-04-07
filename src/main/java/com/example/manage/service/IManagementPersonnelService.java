package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-04-06 14:05:07
 * 项目关联人员
 */

public interface IManagementPersonnelService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
