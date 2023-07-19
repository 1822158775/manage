package com.example.manage.controller;

import com.example.manage.service.ILoginRecordService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-07-14 15:14:23
 * 登入表
 */

@RestController
@RequestMapping(value = "/api/login_record/")
public class LoginRecordController {
    @Resource
    private ILoginRecordService iLoginRecordService;

    // 查询登入表
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iLoginRecordService.methodMaster(request,"cat");
    }

    // 添加登入表
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iLoginRecordService.methodMaster(request,"add");
    }

    // 修改登入表
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iLoginRecordService.methodMaster(request,"edit");
    }
}
