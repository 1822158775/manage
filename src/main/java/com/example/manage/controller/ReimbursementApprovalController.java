package com.example.manage.controller;

import com.example.manage.service.IReimbursementApprovalService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-04-11 15:22:19
 * 报销记录关联审批人进行审批
 */

@RestController
@RequestMapping(value = "/api/reimbursement_approval/")
public class ReimbursementApprovalController {
    @Resource
    private IReimbursementApprovalService iReimbursementApprovalService;

    // 查询报销记录关联审批人进行审批
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iReimbursementApprovalService.methodMaster(request,"cat");
    }

    // 添加报销记录关联审批人进行审批
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iReimbursementApprovalService.methodMaster(request,"add");
    }

    // 修改报销记录关联审批人进行审批
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iReimbursementApprovalService.methodMaster(request,"edit");
    }
}
