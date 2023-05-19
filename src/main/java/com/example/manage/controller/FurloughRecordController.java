package com.example.manage.controller;

import com.example.manage.service.IFurloughRecordService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-05-17 11:15:58
 * 请假记录表
 */

@RestController
@RequestMapping(value = "/api/furlough_record/")
public class FurloughRecordController {
    @Resource
    private IFurloughRecordService iFurloughRecordService;

    // 查询请假记录表
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iFurloughRecordService.methodMaster(request,"cat");
    }

    // 添加请假记录表
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iFurloughRecordService.methodMaster(request,"add");
    }

    // 修改请假记录表
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iFurloughRecordService.methodMaster(request,"edit");
    }
}
