package com.example.manage.controller;

import com.example.manage.service.IManageRCService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-04-11 14:25:04
 * 报销类目关联特殊条件管理
 */

@RestController
@RequestMapping(value = "/api/manage_r_c/")
public class ManageRCController {
    @Resource
    private IManageRCService iManageRCService;

    // 查询报销类目关联特殊条件管理
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iManageRCService.methodMaster(request,"cat");
    }

    // 添加报销类目关联特殊条件管理
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iManageRCService.methodMaster(request,"add");
    }

    // 修改报销类目关联特殊条件管理
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iManageRCService.methodMaster(request,"edit");
    }
}
