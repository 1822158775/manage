package com.example.manage.controller;

import com.example.manage.service.ISignInReviewService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2024-08-02 10:59:24
 * 视频签到申请表
 */

@RestController
@RequestMapping(value = "/api/sign_in_review/")
public class SignInReviewController {
    @Resource
    private ISignInReviewService iSignInReviewService;

    // 查询视频签到申请表
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iSignInReviewService.methodMaster(request,"cat");
    }

    // 添加视频签到申请表
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iSignInReviewService.methodMaster(request,"add");
    }

    // 修改视频签到申请表
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iSignInReviewService.methodMaster(request,"edit");
    }
}
