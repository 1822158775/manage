package com.example.manage.controller;

import com.example.manage.service.IManageRMService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-04-10 18:27:51
 * 类目关联审批人管理
 */

@RestController
@RequestMapping(value = "/api/manage_r_m/")
public class ManageRMController {
    @Resource
    private IManageRMService iManageRMService;

    // 查询类目关联审批人管理
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iManageRMService.methodMaster(request,"cat");
    }

    // 添加类目关联审批人管理
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iManageRMService.methodMaster(request,"add");
    }

    // 修改类目关联审批人管理
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iManageRMService.methodMaster(request,"edit");
    }
}
