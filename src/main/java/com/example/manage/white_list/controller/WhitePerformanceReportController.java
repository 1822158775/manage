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

    //提交业绩数据
    @PostMapping(value = "add")
    private ReturnEntity add(HttpServletRequest request){
        synchronized (this.getClass()) {
            return iWhitePerformanceReportService.methodMaster(request, "add");
        }
    }

    //查询业绩数据
    @PostMapping(value = "cat")
    private ReturnEntity cat(HttpServletRequest request){
        return iWhitePerformanceReportService.methodMaster(request,"cat");
    }

    //查询业绩数据
    @PostMapping(value = "cat_audit")
    private ReturnEntity cat_audit(HttpServletRequest request){
        return iWhitePerformanceReportService.methodMaster(request,"cat_audit");
    }

    //审批业绩
    @PostMapping(value = "edit")
    private ReturnEntity edit(HttpServletRequest request){
        synchronized (this.getClass()) {
            return iWhitePerformanceReportService.methodMaster(request, "edit");
        }
    }

    //查询业绩审批激活数据
    @PostMapping(value = "cat_audit_number")
    public ReturnEntity cat_audit_number(HttpServletRequest request){
        return iWhitePerformanceReportService.methodMaster(request,"cat_audit_number");
    }

    //查询各项状态的数据
    @PostMapping(value = "cat_number")
    public ReturnEntity cat_number(HttpServletRequest request){
        return iWhitePerformanceReportService.methodMaster(request,"cat_number");
    }
}
