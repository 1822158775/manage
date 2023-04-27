package com.example.manage.white_list.controller;

import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteSysManagementService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023/3/31
 * 项目接口
 */
@RestController
@RequestMapping(value = "/api/white_list/sys_management/")
public class WhiteSysManagementController {
    @Resource
    private IWhiteSysManagementService iWhiteSysManagementService;

    // 查询项目
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iWhiteSysManagementService.methodMaster(request,"cat");
    }

    // 查询项目人员编码
    @PostMapping(value = "cat_number")
    public ReturnEntity cat_number(HttpServletRequest request){
        return iWhiteSysManagementService.methodMaster(request,"cat_number");
    }
}
