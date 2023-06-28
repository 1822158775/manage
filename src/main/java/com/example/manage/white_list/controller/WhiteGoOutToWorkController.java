package com.example.manage.white_list.controller;

import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteGoOutToWorkService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023/6/26
 * 出差记录表
 */

@RestController
@RequestMapping(value = "/api/white_list/go_out_to_work/")
public class WhiteGoOutToWorkController {
    @Resource
    private IWhiteGoOutToWorkService iWhiteGoOutToWorkService;

    // 查询出差记录表
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iWhiteGoOutToWorkService.methodMaster(request,"cat");
    }

    // 查询历史提交请假记录表
    @PostMapping(value = "cat_past_records")
    public ReturnEntity cat_past_records(HttpServletRequest request){
        return iWhiteGoOutToWorkService.methodMaster(request,"cat_past_records");
    }

    // 查询历史审核请假记录表
    @PostMapping(value = "cat_collate_past_records")
    public ReturnEntity cat_collate_past_records(HttpServletRequest request){
        return iWhiteGoOutToWorkService.methodMaster(request,"cat_collate_past_records");
    }

    // 添加出差记录表
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        synchronized (this.getClass()) {
            return iWhiteGoOutToWorkService.methodMasterT(request, "add");
        }
    }

    // 修改出差记录表
    @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        synchronized (this.getClass()) {
            return iWhiteGoOutToWorkService.methodMasterT(request, "edit");
        }
    }
}