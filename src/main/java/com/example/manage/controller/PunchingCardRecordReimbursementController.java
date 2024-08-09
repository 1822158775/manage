package com.example.manage.controller;

import com.example.manage.service.IPunchingCardRecordReimbursementService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2024-08-09 15:11:58
 * 签到审核表
 */

@RestController
@RequestMapping(value = "/api/punching_card_record_reimbursement/")
public class PunchingCardRecordReimbursementController {
    @Resource
    private IPunchingCardRecordReimbursementService iPunchingCardRecordReimbursementService;

    // 查询签到审核表
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iPunchingCardRecordReimbursementService.methodMaster(request,"cat");
    }

    // 添加签到审核表
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iPunchingCardRecordReimbursementService.methodMaster(request,"add");
    }

    // 修改签到审核表
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iPunchingCardRecordReimbursementService.methodMaster(request,"edit");
    }
}
