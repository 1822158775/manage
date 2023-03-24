package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-03-24 15:26:30
 * 角色权限
 */

public interface ISysTableAuthorityService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
