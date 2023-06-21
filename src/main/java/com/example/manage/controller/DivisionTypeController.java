package com.example.manage.controller;

import com.example.manage.service.IDivisionTypeService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-06-19 11:06:56
 * 部门类型关联部门管理表
 */

@RestController
@RequestMapping(value = "/api/division_type/")
public class DivisionTypeController {
    @Resource
    private IDivisionTypeService iDivisionTypeService;

    // 查询部门类型关联部门管理表
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iDivisionTypeService.methodMaster(request,"cat");
    }

    // 添加部门类型关联部门管理表
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iDivisionTypeService.methodMaster(request,"add");
    }

    // 修改部门类型关联部门管理表
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iDivisionTypeService.methodMaster(request,"edit");
    }
}
