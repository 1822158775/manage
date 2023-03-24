package com.example.manage.controller;

import com.example.manage.service.ISysAuditManagementService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023/3/24
 */
@RestController
@RequestMapping(value = "/api/sys_audit_management/")
public class SysAuditManagementController {
    @Resource
    private ISysAuditManagementService iService;
    // 查询项目
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iService.methodMaster(request,"cat");
    }

    // 添加项目
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iService.methodMaster(request,"add");
    }

    // 修改项目
    @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iService.methodMaster(request,"edit");
    }
}
