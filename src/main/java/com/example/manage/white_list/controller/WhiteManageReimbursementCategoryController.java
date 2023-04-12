package com.example.manage.white_list.controller;

import com.example.manage.service.IManageReimbursementCategoryService;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteManageReimbursementCategoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023/4/12
 * 类目管理
 */

@RestController
@RequestMapping(value = "/api/white_list/manage_reimbursement_category/")
public class WhiteManageReimbursementCategoryController {
    @Resource
    private IWhiteManageReimbursementCategoryService iWhiteManageReimbursementCategoryService;

    // 查询类目管理
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iWhiteManageReimbursementCategoryService.methodMaster(request,"cat");
    }

}
