package com.example.manage.white_list.controller;

import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteFeedbackRecordSheetService;
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
@RequestMapping(value = "/api/white_list/feedback_record_sheet/")
public class WhiteFeedbackRecordSheetController {
    @Resource
    private IWhiteFeedbackRecordSheetService iWhiteFeedbackRecordSheetService;

    // 查询反馈记录表
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iWhiteFeedbackRecordSheetService.methodMaster(request,"cat");
    }

    // 添加反馈记录表
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iWhiteFeedbackRecordSheetService.methodMaster(request,"add");
    }

    // 修改反馈记录表
    @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iWhiteFeedbackRecordSheetService.methodMaster(request,"edit");
    }
}
