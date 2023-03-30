package com.example.manage.controller;

import com.example.manage.service.IManageCardTypeService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-03-30 16:23:53
 * 项目关联卡种
 */

@RestController
@RequestMapping(value = "/api/manage_card_type/")
public class ManageCardTypeController {
    @Resource
    private IManageCardTypeService iManageCardTypeService;

    // 查询项目关联卡种
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iManageCardTypeService.methodMaster(request,"cat");
    }

    // 添加项目关联卡种
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iManageCardTypeService.methodMaster(request,"add");
    }

    // 修改项目关联卡种
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iManageCardTypeService.methodMaster(request,"edit");
    }
}
