package com.example.manage.controller;

import com.example.manage.service.IReimbursementProjectService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-04-11 15:27:20
 * 报销记录关联项目
 */

@RestController
@RequestMapping(value = "/api/reimbursement_project/")
public class ReimbursementProjectController {
    @Resource
    private IReimbursementProjectService iReimbursementProjectService;

    // 查询报销记录关联项目
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iReimbursementProjectService.methodMaster(request,"cat");
    }

    // 添加报销记录关联项目
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iReimbursementProjectService.methodMaster(request,"add");
    }

    // 修改报销记录关联项目
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iReimbursementProjectService.methodMaster(request,"edit");
    }
}
