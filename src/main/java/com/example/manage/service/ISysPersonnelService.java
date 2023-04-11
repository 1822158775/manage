package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023/3/23
 */

public interface ISysPersonnelService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
    ReturnEntity methodMasterT(HttpServletRequest request, String name);
}
