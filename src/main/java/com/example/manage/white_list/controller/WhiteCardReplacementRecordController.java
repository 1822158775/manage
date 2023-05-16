package com.example.manage.white_list.controller;

import com.example.manage.service.ICardReplacementRecordService;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteCardReplacementRecordService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-05-16 09:47:03
 * 补卡
 */

@RestController
@RequestMapping(value = "/api/white_list/card_replacement_record/")
public class WhiteCardReplacementRecordController {
    @Resource
    private IWhiteCardReplacementRecordService iWhiteCardReplacementRecordService;

    // 查询补卡
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iWhiteCardReplacementRecordService.methodMaster(request,"cat");
    }

    // 查询历史提交的补卡
    @PostMapping(value = "cat_past_records")
    public ReturnEntity cat_past_records(HttpServletRequest request){
        return iWhiteCardReplacementRecordService.methodMaster(request,"cat_past_records");
    }

    //查询过往审核的数据
    @PostMapping(value = "cat_collate_past_records")
    public ReturnEntity cat_collate_past_records(HttpServletRequest request){
        return iWhiteCardReplacementRecordService.methodMaster(request,"cat_collate_past_records");
    }

    // 添加补卡
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iWhiteCardReplacementRecordService.methodMasterT(request,"add");
    }

    // 修改补卡
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iWhiteCardReplacementRecordService.methodMasterT(request,"edit");
    }
}
