package com.example.manage.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @avthor 潘小章
 * @date 2022/9/23
 */
@RestController
public class HelloController {

    @RequestMapping(value = "/")
    public String hello(){
        return "欢迎您的访问!";
    }
}
