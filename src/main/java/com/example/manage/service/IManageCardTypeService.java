package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-03-30 16:23:53
 * 项目关联卡种
 */

public interface IManageCardTypeService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
