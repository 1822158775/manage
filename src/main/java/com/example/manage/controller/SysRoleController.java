package com.example.manage.controller;

import com.example.manage.service.ISysRoleService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023/3/23
 * 角色接口
 */

@RestController
@RequestMapping(value = "/api/sys_role/")
public class SysRoleController {
    @Resource
    private ISysRoleService iSysRoleService;

    // 查询角色
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iSysRoleService.methodMaster(request,"cat");
    }

    // 添加角色
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iSysRoleService.methodMaster(request,"add");
    }

    // 修改角色
    @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iSysRoleService.methodMaster(request,"edit");
    }
}
