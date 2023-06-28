package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-06-26 15:54:22
 * 出差审核表
 */

public interface IGoOutToWorkReimbursementService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
