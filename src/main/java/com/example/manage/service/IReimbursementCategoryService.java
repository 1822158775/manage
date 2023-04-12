package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-04-11 15:26:31
 * 报销记录关联类目
 */

public interface IReimbursementCategoryService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
