package com.example.manage.controller;

import com.example.manage.service.IReimbursementImageService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-04-11 15:34:07
 * 图片存储
 */

@RestController
@RequestMapping(value = "/api/reimbursement_image/")
public class ReimbursementImageController {
    @Resource
    private IReimbursementImageService iReimbursementImageService;

    // 查询图片存储
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iReimbursementImageService.methodMaster(request,"cat");
    }

    // 添加图片存储
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        return iReimbursementImageService.methodMaster(request,"add");
    }

    // 修改图片存储
   @PostMapping(value = "edit")
    public ReturnEntity exit(HttpServletRequest request){
        return iReimbursementImageService.methodMaster(request,"edit");
    }
}
