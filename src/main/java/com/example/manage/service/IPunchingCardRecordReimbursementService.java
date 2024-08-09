package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2024-08-09 15:11:58
 * 签到审核表
 */

public interface IPunchingCardRecordReimbursementService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
