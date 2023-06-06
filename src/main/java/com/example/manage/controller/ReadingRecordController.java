package com.example.manage.controller;

import com.example.manage.service.IReadingRecordService;
import com.example.manage.util.entity.ReturnEntity;
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
@RequestMapping(value = "/api/reading_record/")
public class ReadingRecordController {
    @Resource
    private IReadingRecordService iReadingRecordService;

    // 查询阅读记录
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iReadingRecordService.methodMaster(request,"cat");
    }

    // 添加阅读记录
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iReadingRecordService.methodMaster(request,"add");
    }

    // 修改阅读记录
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iReadingRecordService.methodMaster(request,"edit");
    }
}
