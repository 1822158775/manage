package com.example.manage.controller;

import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @avthor 潘小章
 * @date 2022/4/22
 */
@RestController
@RequestMapping(value = "/api/error/")
public class ErrorController {
    @RequestMapping(value = "getBack")
    public ReturnEntity getBack(){
        return new ReturnEntity(
                CodeEntity.CODE_300,
                MsgEntity.CODE_300
        );
    }
    @RequestMapping(value = "getRight")
    public ReturnEntity getRight(){
        return new ReturnEntity(
                CodeEntity.CODE_422,
                MsgEntity.CODE_422
        );
    }
}
