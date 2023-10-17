package com.example.manage.service;

import com.example.manage.entity.SysTableAuthority;
import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023-03-24 15:26:30
 * 角色权限
 */

public interface ISysTableAuthorityService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
