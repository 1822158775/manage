package com.example.manage.controller;

import com.example.manage.service.IFurloughReimbursementService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-05-17 11:45:41
 * 请假审核表
 */

@RestController
@RequestMapping(value = "/api/furlough_reimbursement/")
public class FurloughReimbursementController {
    @Resource
    private IFurloughReimbursementService iFurloughReimbursementService;

    // 查询请假审核表
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iFurloughReimbursementService.methodMaster(request,"cat");
    }

    // 添加请假审核表
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iFurloughReimbursementService.methodMaster(request,"add");
    }

    // 修改请假审核表
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iFurloughReimbursementService.methodMaster(request,"edit");
    }
}
