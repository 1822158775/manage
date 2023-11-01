package com.example.manage.controller;

import com.example.manage.service.IUnbindRecordService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-10-31 09:14:49
 * 解绑记录表
 */

@RestController
@RequestMapping(value = "/api/unbind_record/")
public class UnbindRecordController {
    @Resource
    private IUnbindRecordService iUnbindRecordService;

    // 查询解绑记录表
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iUnbindRecordService.methodMaster(request,"cat");
    }

    // 添加解绑记录表
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iUnbindRecordService.methodMaster(request,"add");
    }

    // 修改解绑记录表
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iUnbindRecordService.methodMaster(request,"edit");
    }
}
