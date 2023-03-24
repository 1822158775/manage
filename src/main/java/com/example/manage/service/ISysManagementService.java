package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023/3/23
 */

public interface ISysManagementService {
    ReturnEntity methodMaster(HttpServletRequest request,String name);
}
