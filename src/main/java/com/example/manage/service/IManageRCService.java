package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-04-11 14:25:03
 * 报销类目关联特殊条件管理
 */

public interface IManageRCService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
