package com.example.manage.white_list.controller;

import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteRankingListService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023/4/20
 * 人员业绩统计
 */

@RestController
@RequestMapping(value = "/api/white_list/ranking_list/")
public class WhiteRankingListController {

    @Resource
    private IWhiteRankingListService iWhiteRankingListService;

    // 查询项目
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iWhiteRankingListService.methodMaster(request,"cat");
    }

    // 查询项目进度条
    @PostMapping(value = "cat_progress_bar")
    public ReturnEntity cat_progress_bar(HttpServletRequest request){
        return iWhiteRankingListService.methodMaster(request,"cat_progress_bar");
    }
}
