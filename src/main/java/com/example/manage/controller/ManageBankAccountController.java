package com.example.manage.controller;

import com.example.manage.service.IManageBankAccountService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-04-11 16:02:00
 * 银行账户管理
 */

@RestController
@RequestMapping(value = "/api/manage_bank_account/")
public class ManageBankAccountController {
    @Resource
    private IManageBankAccountService iManageBankAccountService;

    // 查询银行账户管理
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iManageBankAccountService.methodMaster(request,"cat");
    }

    // 添加银行账户管理
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iManageBankAccountService.methodMaster(request,"add");
    }

    // 修改银行账户管理
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iManageBankAccountService.methodMaster(request,"edit");
    }
}
