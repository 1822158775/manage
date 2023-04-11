package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-04-10 18:28:40
 * 类目关联抄送人管理
 */

public interface ICategoryCopyService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
