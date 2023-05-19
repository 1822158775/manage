package com.example.manage.white_list.controller;

import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhitePunchingCardRecordService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023/5/5
 * 打卡记录表
 */

@RestController
@RequestMapping(value = "/api/white_list/punching_card_record/")
public class WhitePunchingCardRecordController {

    @Resource
    private IWhitePunchingCardRecordService iWhitePunchingCardRecordService;

    // 添加打卡记录表
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        synchronized (this.getClass()){
            return iWhitePunchingCardRecordService.methodMasterT(request,"add");
        }
    }

    // 查询当天打卡记录
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iWhitePunchingCardRecordService.methodMaster(request,"cat");
    }

    // 查询指定日期的打卡记录
    @PostMapping(value = "cat_day")
    public ReturnEntity cat_day(HttpServletRequest request){
        return iWhitePunchingCardRecordService.methodMaster(request,"cat_day");
    }

    // 查询历史打卡记录
    @PostMapping(value = "cat_list")
    public ReturnEntity cat_list(HttpServletRequest request){
        return iWhitePunchingCardRecordService.methodMaster(request,"cat_list");
    }

    // 查询是否在位置
    @PostMapping(value = "area")
    public ReturnEntity area(HttpServletRequest request){
        return iWhitePunchingCardRecordService.methodMaster(request,"area");
    }
}
