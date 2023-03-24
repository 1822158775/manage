package com.example.manage.controller;

import com.example.manage.service.ISysTableAuthorityService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-03-24 15:26:30
 * 角色权限
 */

@RestController
@RequestMapping(value = "/api/sys_table_authority/")
public class SysTableAuthorityController {
    @Resource
    private ISysTableAuthorityService iSysTableAuthorityService;

    // 查询角色权限
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iSysTableAuthorityService.methodMaster(request,"cat");
    }

    // 添加角色权限
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iSysTableAuthorityService.methodMaster(request,"add");
    }

    // 修改角色权限
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iSysTableAuthorityService.methodMaster(request,"edit");
    }
}
