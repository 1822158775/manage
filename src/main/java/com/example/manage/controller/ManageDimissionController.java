package com.example.manage.controller;

import com.example.manage.service.IManageDimissionService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-03-29 11:16:51
 * 离职申请管理
 */

@RestController
@RequestMapping(value = "/api/manage_dimission/")
public class ManageDimissionController {
    @Resource
    private IManageDimissionService iManageDimissionService;

    // 查询离职申请管理
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iManageDimissionService.methodMaster(request,"cat");
    }

    // 添加离职申请管理
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iManageDimissionService.methodMaster(request,"add");
    }

    // 修改离职申请管理
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iManageDimissionService.methodMaster(request,"edit");
    }
}
