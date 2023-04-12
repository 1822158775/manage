package com.example.manage.controller;

import com.example.manage.service.IReimbursementCopyService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-04-11 15:25:56
 * 报销记录抄送人
 */

@RestController
@RequestMapping(value = "/api/reimbursement_copy/")
public class ReimbursementCopyController {
    @Resource
    private IReimbursementCopyService iReimbursementCopyService;

    // 查询报销记录抄送人
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iReimbursementCopyService.methodMaster(request,"cat");
    }

    // 添加报销记录抄送人
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iReimbursementCopyService.methodMaster(request,"add");
    }

    // 修改报销记录抄送人
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iReimbursementCopyService.methodMaster(request,"edit");
    }
}
