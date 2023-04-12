package com.example.manage.controller;

import com.example.manage.service.IManageConditionService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-04-11 14:24:25
 * 特殊条件管理
 */

@RestController
@RequestMapping(value = "/api/manage_condition/")
public class ManageConditionController {
    @Resource
    private IManageConditionService iManageConditionService;

    // 查询特殊条件管理
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iManageConditionService.methodMaster(request,"cat");
    }

    // 添加特殊条件管理
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iManageConditionService.methodMaster(request,"add");
    }

    // 修改特殊条件管理
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iManageConditionService.methodMaster(request,"edit");
    }
}
