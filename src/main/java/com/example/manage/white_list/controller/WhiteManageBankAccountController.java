package com.example.manage.white_list.controller;

import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteManageBankAccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023/4/12
 */

@RestController
@RequestMapping(value = "/api/white_list/manage_bank_account/")
public class WhiteManageBankAccountController {
    @Resource
    private IWhiteManageBankAccountService iWhiteManageBankAccountService;

    // 查询银行账户管理
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iWhiteManageBankAccountService.methodMaster(request,"cat");
    }

    // 添加银行账户管理
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        synchronized (this.getClass()) {
            return iWhiteManageBankAccountService.methodMaster(request, "add");
        }
    }

    // 修改银行账户管理
    @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        synchronized (this.getClass()) {
            return iWhiteManageBankAccountService.methodMaster(request, "edit");
        }
    }
}
