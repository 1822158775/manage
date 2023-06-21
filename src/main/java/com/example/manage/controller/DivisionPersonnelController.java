package com.example.manage.controller;

import com.example.manage.service.IDivisionPersonnelService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-06-21 17:21:22
 * 总经理关联部门
 */

@RestController
@RequestMapping(value = "/api/division_personnel/")
public class DivisionPersonnelController {
    @Resource
    private IDivisionPersonnelService iDivisionPersonnelService;

    // 查询总经理关联部门
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iDivisionPersonnelService.methodMaster(request,"cat");
    }

    // 添加总经理关联部门
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iDivisionPersonnelService.methodMaster(request,"add");
    }

    // 修改总经理关联部门
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iDivisionPersonnelService.methodMaster(request,"edit");
    }
}
