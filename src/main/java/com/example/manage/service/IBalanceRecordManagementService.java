package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-03-27 11:16:09
 * 余额记录管理
 */

public interface IBalanceRecordManagementService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
