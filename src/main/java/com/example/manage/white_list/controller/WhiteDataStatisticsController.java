package com.example.manage.white_list.controller;

import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteDataStatisticsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023/4/19
 * 数据统计
 */

@RestController
@RequestMapping(value = "/api/white_list/data_statistics/")
public class WhiteDataStatisticsController {
    @Resource
    private IWhiteDataStatisticsService iWhiteDataStatisticsService;

    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iWhiteDataStatisticsService.methodMaster(request,"cat");
    }
}
