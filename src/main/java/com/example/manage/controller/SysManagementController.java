package com.example.manage.controller;

import com.example.manage.service.ISysManagementService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023/3/23
 * 项目接口
 */
@RestController
@RequestMapping(value = "/api/sys_management/")
public class SysManagementController {
    @Resource
    private ISysManagementService iSysManagementService;

    // 查询项目
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iSysManagementService.methodMaster(request,"cat");
    }

    // 添加项目
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iSysManagementService.methodMaster(request,"add");
    }

    // 修改项目
    @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iSysManagementService.methodMaster(request,"edit");
    }
}
