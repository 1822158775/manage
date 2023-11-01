package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-10-31 09:14:49
 * 解绑记录表
 */

public interface IUnbindRecordService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
