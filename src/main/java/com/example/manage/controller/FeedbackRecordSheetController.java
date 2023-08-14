package com.example.manage.controller;

import com.example.manage.service.IFeedbackRecordSheetService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-08-01 10:52:38
 * 反馈记录表
 */

@RestController
@RequestMapping(value = "/api/feedback_record_sheet/")
public class FeedbackRecordSheetController {
    @Resource
    private IFeedbackRecordSheetService iFeedbackRecordSheetService;

    // 查询反馈记录表
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iFeedbackRecordSheetService.methodMaster(request,"cat");
    }

    // 添加反馈记录表
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iFeedbackRecordSheetService.methodMaster(request,"add");
    }

    // 修改反馈记录表
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iFeedbackRecordSheetService.methodMaster(request,"edit");
    }
}
