package com.example.manage.controller;

import com.example.manage.service.IDivisionManagementService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-06-19 11:30:35
 * 部门管理表
 */

@RestController
@RequestMapping(value = "/api/division_management/")
public class DivisionManagementController {
    @Resource
    private IDivisionManagementService iDivisionManagementService;

    // 查询部门管理表
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iDivisionManagementService.methodMaster(request,"cat");
    }

    // 添加部门管理表
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iDivisionManagementService.methodMaster(request,"add");
    }

    // 修改部门管理表
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iDivisionManagementService.methodMaster(request,"edit");
    }
}
