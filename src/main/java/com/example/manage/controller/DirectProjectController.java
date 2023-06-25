package com.example.manage.controller;

import com.example.manage.service.IDirectProjectService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-06-25 17:25:35
 * 部门直管项目表
 */

@RestController
@RequestMapping(value = "/api/direct_project/")
public class DirectProjectController {
    @Resource
    private IDirectProjectService iDirectProjectService;

    // 查询部门直管项目表
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iDirectProjectService.methodMaster(request,"cat");
    }

    // 添加部门直管项目表
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iDirectProjectService.methodMaster(request,"add");
    }

    // 修改部门直管项目表
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iDirectProjectService.methodMaster(request,"edit");
    }
}
