package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-03-29 16:50:54
 * 业绩记录管理
 */

public interface IPerformanceReportService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
