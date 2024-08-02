package com.example.manage.controller;

import com.example.manage.service.ISignInReviewReimbursementService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2024-08-02 11:00:15
 * 视频签到审核表
 */

@RestController
@RequestMapping(value = "/api/sign_in_review_reimbursement/")
public class SignInReviewReimbursementController {
    @Resource
    private ISignInReviewReimbursementService iSignInReviewReimbursementService;

    // 查询视频签到审核表
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iSignInReviewReimbursementService.methodMaster(request,"cat");
    }

    // 添加视频签到审核表
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iSignInReviewReimbursementService.methodMaster(request,"add");
    }

    // 修改视频签到审核表
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iSignInReviewReimbursementService.methodMaster(request,"edit");
    }
}
