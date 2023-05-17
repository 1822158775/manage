package com.example.manage.white_list.controller;

import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteSysPersonnelService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023/3/31
 * 人员管理
 */

@RestController
@RequestMapping(value = "/api/white_list/sys_personnel/")
public class WhiteSysPersonnelController {

    @Resource
    private IWhiteSysPersonnelService iWhiteSysPersonnelService;

    // 查询项目
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iWhiteSysPersonnelService.methodMaster(request,"cat");
    }

    // 查询项目
    @PostMapping(value = "cat_list")
    public ReturnEntity cat_list(HttpServletRequest request){
        return iWhiteSysPersonnelService.methodMaster(request,"cat_list");
    }
    // 查询同项目组下一级人员
    @PostMapping(value = "cat_new_list")
    public ReturnEntity cat_new_list(HttpServletRequest request){
        return iWhiteSysPersonnelService.methodMaster(request,"cat_new_list");
    }

    // 添加员工
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iWhiteSysPersonnelService.methodMasterT(request,"add");
    }

    // 编辑
    @PostMapping(value = "edit")
    public ReturnEntity edit(HttpServletRequest request){
        return iWhiteSysPersonnelService.methodMasterT(request,"edit");
    }
}
