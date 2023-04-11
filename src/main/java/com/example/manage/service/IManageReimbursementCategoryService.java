package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-04-10 18:23:08
 * 类目管理
 */

public interface IManageReimbursementCategoryService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
    ReturnEntity methodMasterT(HttpServletRequest request, String name);
}
