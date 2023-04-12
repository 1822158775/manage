package com.example.manage.white_list.controller;

import com.example.manage.service.IManageReimbursementRecordService;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteManageReimbursementRecordService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023/4/11
 */


@RestController
@RequestMapping(value = "/api/white_list/manage_reimbursement_record/")
public class WhiteManageReimbursementRecordController {
    @Resource
    private IWhiteManageReimbursementRecordService iWhiteManageReimbursementRecordService;

    // 查询申请报销记录管理
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iWhiteManageReimbursementRecordService.methodMaster(request,"cat");
    }

    // 信息填写
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iWhiteManageReimbursementRecordService.methodMasterT(request,"add");
    }

    // 修改申请报销记录管理
    @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iWhiteManageReimbursementRecordService.methodMasterT(request,"edit");
    }
}
