package com.example.manage.white_list.controller;

import com.example.manage.service.ICardTypeSalesService;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteCardTypeSalesService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023/6/19
 * 信用卡关联权益
 */

@RestController
@RequestMapping(value = "/api/white_list/card_type_sales/")
public class WhiteCardTypeSalesController {

    @Resource
    private IWhiteCardTypeSalesService iWhiteCardTypeSalesService;

    // 查询信用卡关联权益
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iWhiteCardTypeSalesService.methodMaster(request,"cat");
    }
}
