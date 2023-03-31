package com.example.manage.white_list.controller;

import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhitePerformanceReportService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023/3/31
 */

@RestController
@RequestMapping(value = "/api/white_list/performance_report/")
public class WhitePerformanceReportController {

    @Resource
    private IWhitePerformanceReportService iWhitePerformanceReportService;

    @PostMapping(value = "add")
    private ReturnEntity add(HttpServletRequest request){
        return iWhitePerformanceReportService.methodMaster(request,"add");
    }
}
