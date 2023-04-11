package com.example.manage.controller;

import com.example.manage.service.IManageReimbursementCategoryService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-04-10 18:23:08
 * 类目管理
 */

@RestController
@RequestMapping(value = "/api/manage_reimbursement_category/")
public class ManageReimbursementCategoryController {
    @Resource
    private IManageReimbursementCategoryService iManageReimbursementCategoryService;

    // 查询类目管理
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iManageReimbursementCategoryService.methodMaster(request,"cat");
    }

    // 添加类目管理
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iManageReimbursementCategoryService.methodMasterT(request,"add");
    }

    // 修改类目管理
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iManageReimbursementCategoryService.methodMasterT(request,"edit");
    }
}
