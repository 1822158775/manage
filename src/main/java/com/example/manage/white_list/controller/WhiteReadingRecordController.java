package com.example.manage.white_list.controller;


import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteReadingRecordService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-06-01 14:43:43
 * 阅读记录
 */

@RestController
@RequestMapping(value = "/api/white_list/reading_record/")
public class WhiteReadingRecordController {
    @Resource
    private IWhiteReadingRecordService iWhiteReadingRecordService;

    // 查询阅读记录
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iWhiteReadingRecordService.methodMaster(request,"cat");
    }

    // 添加阅读记录
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iWhiteReadingRecordService.methodMasterT(request,"add");
    }

    // 修改阅读记录
    @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iWhiteReadingRecordService.methodMaster(request,"edit");
    }
}
