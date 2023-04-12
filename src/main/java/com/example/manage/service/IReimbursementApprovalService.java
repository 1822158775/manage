package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-04-11 15:22:19
 * 报销记录关联审批人进行审批
 */

public interface IReimbursementApprovalService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
