package com.example.manage.controller;

import com.example.manage.service.ICardReplacementRecordService;
import com.example.manage.util.entity.ReturnEntity;
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
@RequestMapping(value = "/api/card_replacement_record/")
public class CardReplacementRecordController {
    @Resource
    private ICardReplacementRecordService iCardReplacementRecordService;

    // 查询补卡
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iCardReplacementRecordService.methodMaster(request,"cat");
    }

    // 添加补卡
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iCardReplacementRecordService.methodMaster(request,"add");
    }

    // 修改补卡
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iCardReplacementRecordService.methodMaster(request,"edit");
    }
}
