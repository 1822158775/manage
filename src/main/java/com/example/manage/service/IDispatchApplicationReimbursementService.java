package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-04-26 11:17:41
 * 调派关联审批人
 */

public interface IDispatchApplicationReimbursementService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
