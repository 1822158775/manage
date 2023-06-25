package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-06-25 17:25:35
 * 部门直管项目表
 */

public interface IDirectProjectService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
