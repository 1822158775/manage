package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-06-16 11:43:54
 * 业绩关联权益
 */

public interface IPerformanceReportSalesService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
