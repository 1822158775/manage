package com.example.manage.controller;

import com.example.manage.service.IManagementPersonnelService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-04-06 14:05:07
 * 项目关联人员
 */

@RestController
@RequestMapping(value = "/api/management_personnel/")
public class ManagementPersonnelController {
    @Resource
    private IManagementPersonnelService iManagementPersonnelService;

    // 查询项目关联人员
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iManagementPersonnelService.methodMaster(request,"cat");
    }

    // 添加项目关联人员
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iManagementPersonnelService.methodMaster(request,"add");
    }

    // 修改项目关联人员
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iManagementPersonnelService.methodMaster(request,"edit");
    }
}
