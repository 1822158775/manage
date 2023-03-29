package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-03-29 11:16:51
 * 离职申请管理
 */

public interface IManageDimissionService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
