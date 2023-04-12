package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-04-11 15:25:56
 * 报销记录抄送人
 */

public interface IReimbursementCopyService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
