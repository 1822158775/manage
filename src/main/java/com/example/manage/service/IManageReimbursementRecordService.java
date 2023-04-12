package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-04-11 15:00:11
 * 申请报销记录管理
 */

public interface IManageReimbursementRecordService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
