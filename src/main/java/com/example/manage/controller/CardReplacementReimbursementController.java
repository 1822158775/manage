package com.example.manage.controller;

import com.example.manage.service.ICardReplacementReimbursementService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-05-16 10:30:12
 * 补卡审核人
 */

@RestController
@RequestMapping(value = "/api/card_replacement_reimbursement/")
public class CardReplacementReimbursementController {
    @Resource
    private ICardReplacementReimbursementService iCardReplacementReimbursementService;

    // 查询补卡审核人
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iCardReplacementReimbursementService.methodMaster(request,"cat");
    }

    // 添加补卡审核人
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iCardReplacementReimbursementService.methodMaster(request,"add");
    }

    // 修改补卡审核人
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iCardReplacementReimbursementService.methodMaster(request,"edit");
    }
}
