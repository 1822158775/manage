package com.example.manage.controller;

import com.example.manage.service.IGoOutToWorkService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-06-26 15:52:41
 * 出差记录表
 */

@RestController
@RequestMapping(value = "/api/go_out_to_work/")
public class GoOutToWorkController {
    @Resource
    private IGoOutToWorkService iGoOutToWorkService;

    // 查询出差记录表
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iGoOutToWorkService.methodMaster(request,"cat");
    }

    // 添加出差记录表
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iGoOutToWorkService.methodMaster(request,"add");
    }

    // 修改出差记录表
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iGoOutToWorkService.methodMaster(request,"edit");
    }
}
