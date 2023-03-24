package com.example.manage.controller;

import com.example.manage.service.ISysTableNameService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-03-24 15:05:37
 * 数据表名称
 */

@RestController
@RequestMapping(value = "/api/sys_table_name/")
public class SysTableNameController {
    @Resource
    private ISysTableNameService iSysTableNameService;

    // 查询数据表名称
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iSysTableNameService.methodMaster(request,"cat");
    }

    // 添加数据表名称
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iSysTableNameService.methodMaster(request,"add");
    }

    // 修改数据表名称
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iSysTableNameService.methodMaster(request,"edit");
    }
}
