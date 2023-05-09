package com.example.manage.controller;

import com.example.manage.service.IPunchingCardRecordService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-05-05 15:41:40
 * 打卡记录表
 */

@RestController
@RequestMapping(value = "/api/punching_card_record/")
public class PunchingCardRecordController {
    @Resource
    private IPunchingCardRecordService iPunchingCardRecordService;

    // 查询打卡记录表
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iPunchingCardRecordService.methodMaster(request,"cat");
    }

    // 添加打卡记录表
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iPunchingCardRecordService.methodMaster(request,"add");
    }

    // 修改打卡记录表
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iPunchingCardRecordService.methodMaster(request,"edit");
    }

    //查询数据统计
    @PostMapping(value = "statistics")
    public ReturnEntity statistics(HttpServletRequest request){
        return iPunchingCardRecordService.methodMaster(request,"statistics");
    }
}
