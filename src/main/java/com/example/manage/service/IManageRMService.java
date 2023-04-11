package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-04-10 18:27:51
 * 类目关联审批人管理
 */

public interface IManageRMService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
