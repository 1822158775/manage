package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-06-26 15:52:40
 * 出差记录表
 */

public interface IGoOutToWorkService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
