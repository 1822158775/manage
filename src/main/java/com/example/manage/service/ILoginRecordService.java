package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-07-14 15:14:23
 * 登入表
 */

public interface ILoginRecordService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
