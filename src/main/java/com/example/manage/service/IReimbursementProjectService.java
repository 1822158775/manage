package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-04-11 15:27:20
 * 报销记录关联项目
 */

public interface IReimbursementProjectService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
