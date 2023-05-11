package com.example.manage.white_list.controller;

import com.example.manage.service.ICheckInTimeService;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteCheckInTimeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023/5/10
 */

@RestController
@RequestMapping(value = "/api/white_list/check_in_time/")
public class WhiteCheckInTimeController {
    @Resource
    private IWhiteCheckInTimeService iWhiteCheckInTimeService;

    // 查询打卡时间表
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iWhiteCheckInTimeService.methodMaster(request,"cat");
    }

    // 添加打卡时间表
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iWhiteCheckInTimeService.methodMaster(request,"add");
    }

    // 修改打卡时间表
    @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iWhiteCheckInTimeService.methodMaster(request,"edit");
    }
}

