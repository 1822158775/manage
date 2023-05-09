package com.example.manage.controller;

import com.example.manage.service.ISysPersonnelService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023/3/23
 * 人员管理接口
 */
@RestController
@RequestMapping(value = "/api/sys_personnel/")
public class SysPersonnelServiceController {

    @Resource
    private ISysPersonnelService iSysPersonnelService;

    // 查询人员管理接口
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iSysPersonnelService.methodMaster(request,"cat");
    }

    // 添加人员信息
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iSysPersonnelService.methodMasterT(request,"add");
    }

    // 修改人员信息
    @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iSysPersonnelService.methodMasterT(request,"edit");
    }
}
