package com.example.manage.controller;

import com.example.manage.service.ILoginService;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @avthor 潘小章
 * @date 2022/4/15
 */
@RestController
@RequestMapping(value = "/api/login/")
public class LoginController {
    @Resource
    private ILoginService iLoginService;

    /**
     * 登录功能模块
     * @param request
     * @param response
     * @param session
     * @return
     */
    @PostMapping(value = "sign")
    public ReturnEntity sign(HttpServletRequest request, HttpServletResponse response,HttpSession session){
        return iLoginService.login(request,session);
    }
}
