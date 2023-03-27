package com.example.manage.controller;

import com.example.manage.service.IBalanceRecordManagementService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-03-27 11:16:09
 * 余额记录管理
 */

@RestController
@RequestMapping(value = "/api/balance_record_management/")
public class BalanceRecordManagementController {
    @Resource
    private IBalanceRecordManagementService iBalanceRecordManagementService;

    // 查询余额记录管理
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iBalanceRecordManagementService.methodMaster(request,"cat");
    }

    // 添加余额记录管理
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iBalanceRecordManagementService.methodMaster(request,"add");
    }

    // 修改余额记录管理
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iBalanceRecordManagementService.methodMaster(request,"edit");
    }
}
