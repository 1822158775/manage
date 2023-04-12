package com.example.manage.controller;

import com.example.manage.service.IManageReimbursementRecordService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-04-11 15:00:11
 * 申请报销记录管理
 */

@RestController
@RequestMapping(value = "/api/manage_reimbursement_record/")
public class ManageReimbursementRecordController {
    @Resource
    private IManageReimbursementRecordService iManageReimbursementRecordService;

    // 查询申请报销记录管理
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iManageReimbursementRecordService.methodMaster(request,"cat");
    }

    // 添加申请报销记录管理
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iManageReimbursementRecordService.methodMaster(request,"add");
    }

    // 修改申请报销记录管理
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iManageReimbursementRecordService.methodMaster(request,"edit");
    }
}
