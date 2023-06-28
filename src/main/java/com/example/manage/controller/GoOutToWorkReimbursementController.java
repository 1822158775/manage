package com.example.manage.controller;

import com.example.manage.service.IGoOutToWorkReimbursementService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-06-26 15:54:22
 * 出差审核表
 */

@RestController
@RequestMapping(value = "/api/go_out_to_work_reimbursement/")
public class GoOutToWorkReimbursementController {
    @Resource
    private IGoOutToWorkReimbursementService iGoOutToWorkReimbursementService;

    // 查询出差审核表
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iGoOutToWorkReimbursementService.methodMaster(request,"cat");
    }

    // 添加出差审核表
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iGoOutToWorkReimbursementService.methodMaster(request,"add");
    }

    // 修改出差审核表
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iGoOutToWorkReimbursementService.methodMaster(request,"edit");
    }
}
