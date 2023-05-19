package com.example.manage.white_list.controller;

import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteManageDimissionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023/4/3
 * 离职申请管理
 */

@RestController
@RequestMapping(value = "/api/white_list/manage_dimission/")
public class WhiteManageDimissionController {

    @Resource
    private IWhiteManageDimissionService iWhiteManageDimissionService;

    // 查询离职申请管理
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iWhiteManageDimissionService.methodMaster(request,"cat");
    }
    // 查询离职申请管理
    @PostMapping(value = "cat_leave")
    public ReturnEntity cat_leave(HttpServletRequest request){
        return iWhiteManageDimissionService.methodMaster(request,"cat_leave");
    }

    // 提交离职申请
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        synchronized (this.getClass()) {
            return iWhiteManageDimissionService.methodMaster(request, "add");
        }
    }
}
