package com.example.manage.white_list.controller;

import com.example.manage.service.IFurloughRecordService;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteFurloughRecordService;
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
@RequestMapping(value = "/api/white_list/furlough_record/")
public class WhiteFurloughRecordController {
    @Resource
    private IWhiteFurloughRecordService iWhiteFurloughRecordService;

    // 查询请假记录表
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iWhiteFurloughRecordService.methodMaster(request,"cat");
    }

    // 查询历史提交请假记录表
    @PostMapping(value = "cat_past_records")
    public ReturnEntity cat_past_records(HttpServletRequest request){
        return iWhiteFurloughRecordService.methodMaster(request,"cat_past_records");
    }

    // 查询历史审核请假记录表
    @PostMapping(value = "cat_collate_past_records")
    public ReturnEntity cat_collate_past_records(HttpServletRequest request){
        return iWhiteFurloughRecordService.methodMaster(request,"cat_collate_past_records");
    }

    // 添加请假记录表
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        synchronized (this.getClass()) {
            return iWhiteFurloughRecordService.methodMasterT(request, "add");
        }
    }

    // 修改请假记录表
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
       synchronized (this.getClass()) {
           return iWhiteFurloughRecordService.methodMasterT(request, "edit");
       }
    }
}
