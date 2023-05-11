package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-05-10 17:53:30
 * 打卡时间表
 */

public interface ICheckInTimeService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
