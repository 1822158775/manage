package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-06-21 17:21:22
 * 总经理关联部门
 */

public interface IDivisionPersonnelService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
