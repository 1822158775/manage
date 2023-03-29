package com.example.manage.controller;

import com.example.manage.service.IPerformanceReportService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-03-29 16:50:54
 * 业绩记录管理
 */

@RestController
@RequestMapping(value = "/api/performance_report/")
public class PerformanceReportController {
    @Resource
    private IPerformanceReportService iPerformanceReportService;

    // 查询业绩记录管理
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iPerformanceReportService.methodMaster(request,"cat");
    }

    // 添加业绩记录管理
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iPerformanceReportService.methodMaster(request,"add");
    }

    // 修改业绩记录管理
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iPerformanceReportService.methodMaster(request,"edit");
    }
}
