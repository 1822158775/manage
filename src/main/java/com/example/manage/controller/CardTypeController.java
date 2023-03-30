package com.example.manage.controller;

import com.example.manage.service.ICardTypeService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-03-30 14:11:54
 * 卡种管理
 */

@RestController
@RequestMapping(value = "/api/card_type/")
public class CardTypeController {
    @Resource
    private ICardTypeService iCardTypeService;

    // 查询卡种管理
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iCardTypeService.methodMaster(request,"cat");
    }

    // 添加卡种管理
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iCardTypeService.methodMaster(request,"add");
    }

    // 修改卡种管理
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iCardTypeService.methodMaster(request,"edit");
    }
}
