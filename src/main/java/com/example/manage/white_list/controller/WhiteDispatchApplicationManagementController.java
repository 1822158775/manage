package com.example.manage.white_list.controller;

import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteDispatchApplicationManagementService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023/4/7
 * 调派管理
 */

@RestController
@RequestMapping(value = "/api/white_list/dispatch_application_management/")
public class WhiteDispatchApplicationManagementController {
    @Resource
    private IWhiteDispatchApplicationManagementService iWhiteDispatchApplicationManagementService;

    // 查询调派管理
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iWhiteDispatchApplicationManagementService.methodMaster(request,"cat");
    }

    // 添加调派管理
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iWhiteDispatchApplicationManagementService.methodMasterT(request,"add");
    }

    // 修改调派管理
    @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iWhiteDispatchApplicationManagementService.methodMasterT(request,"edit");
    }


    // 查询过往提交数据
    @PostMapping(value = "cat_past_records")
    public ReturnEntity cat_past_records(HttpServletRequest request){
        return iWhiteDispatchApplicationManagementService.methodMaster(request,"cat_past_records");
    }

    //查询过往审核的数据
    @PostMapping(value = "cat_collate_past_records")
    public ReturnEntity cat_collate_past_records(HttpServletRequest request){
        return iWhiteDispatchApplicationManagementService.methodMaster(request,"cat_collate_past_records");
    }
}
