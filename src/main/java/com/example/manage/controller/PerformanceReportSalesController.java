package com.example.manage.controller;

import com.example.manage.service.IPerformanceReportSalesService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-06-16 11:43:55
 * 业绩关联权益
 */

@RestController
@RequestMapping(value = "/api/performance_report_sales/")
public class PerformanceReportSalesController {
    @Resource
    private IPerformanceReportSalesService iPerformanceReportSalesService;

    // 查询业绩关联权益
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iPerformanceReportSalesService.methodMaster(request,"cat");
    }

    // 添加业绩关联权益
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iPerformanceReportSalesService.methodMaster(request,"add");
    }

    // 修改业绩关联权益
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iPerformanceReportSalesService.methodMaster(request,"edit");
    }
}
