package com.example.manage.controller;

import com.example.manage.service.ICheckInTimeService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-05-10 17:53:30
 * 打卡时间表
 */

@RestController
@RequestMapping(value = "/api/check_in_time/")
public class CheckInTimeController {
    @Resource
    private ICheckInTimeService iCheckInTimeService;

    // 查询打卡时间表
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iCheckInTimeService.methodMaster(request,"cat");
    }

    // 添加打卡时间表
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iCheckInTimeService.methodMaster(request,"add");
    }

    // 修改打卡时间表
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iCheckInTimeService.methodMaster(request,"edit");
    }

    // 删除打卡时间表
    @PostMapping(value = "del")
    public ReturnEntity del(HttpServletRequest request){
        return iCheckInTimeService.methodMaster(request,"del");
    }
}
