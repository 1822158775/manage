package com.example.manage.controller;

import com.example.manage.service.ICardTypeSalesService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-06-16 15:07:18
 * 信用卡关联权益
 */

@RestController
@RequestMapping(value = "/api/card_type_sales/")
public class CardTypeSalesController {
    @Resource
    private ICardTypeSalesService iCardTypeSalesService;

    // 查询信用卡关联权益
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iCardTypeSalesService.methodMaster(request,"cat");
    }

    // 添加信用卡关联权益
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iCardTypeSalesService.methodMaster(request,"add");
    }

    // 修改信用卡关联权益
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iCardTypeSalesService.methodMaster(request,"edit");
    }
}
