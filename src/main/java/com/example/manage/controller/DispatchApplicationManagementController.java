package com.example.manage.controller;

import com.example.manage.service.IDispatchApplicationManagementService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-03-29 11:28:23
 * 调派管理
 */

@RestController
@RequestMapping(value = "/api/dispatch_application_management/")
public class DispatchApplicationManagementController {
    @Resource
    private IDispatchApplicationManagementService iDispatchApplicationManagementService;

    // 查询调派管理
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iDispatchApplicationManagementService.methodMaster(request,"cat");
    }

    // 添加调派管理
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iDispatchApplicationManagementService.methodMaster(request,"add");
    }

    // 修改调派管理
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iDispatchApplicationManagementService.methodMaster(request,"edit");
    }
}
