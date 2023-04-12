package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-04-11 14:24:25
 * 特殊条件管理
 */

public interface IManageConditionService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
