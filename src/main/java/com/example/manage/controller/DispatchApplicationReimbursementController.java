package com.example.manage.controller;

import com.example.manage.service.IDispatchApplicationReimbursementService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-04-26 11:17:41
 * 调派关联审批人
 */

@RestController
@RequestMapping(value = "/api/dispatch_application_reimbursement/")
public class DispatchApplicationReimbursementController {
    @Resource
    private IDispatchApplicationReimbursementService iDispatchApplicationReimbursementService;

    // 查询调派关联审批人
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iDispatchApplicationReimbursementService.methodMaster(request,"cat");
    }

    // 添加调派关联审批人
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iDispatchApplicationReimbursementService.methodMaster(request,"add");
    }

    // 修改调派关联审批人
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iDispatchApplicationReimbursementService.methodMaster(request,"edit");
    }
}
