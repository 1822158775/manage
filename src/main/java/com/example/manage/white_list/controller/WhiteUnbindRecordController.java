package com.example.manage.white_list.controller;

import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteUnbindRecordService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023/10/31
 */

@RestController
@RequestMapping(value = "/api/white_list/unbind_record/")
public class WhiteUnbindRecordController {
    @Resource
    private IWhiteUnbindRecordService iWhiteUnbindRecordService;

    // 查询解绑记录表
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iWhiteUnbindRecordService.methodMaster(request,"cat");
    }

    // 添加解绑记录表
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iWhiteUnbindRecordService.methodMaster(request,"add");
    }

    // 修改解绑记录表
    @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iWhiteUnbindRecordService.methodMaster(request,"edit");
    }
}
