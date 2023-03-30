package com.example.manage.controller.white_list;

import com.example.manage.service.ILoginService;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023/3/30
 */
@RestController
@RequestMapping(value = "/api/white_list/")
public class WhiteListLoginController {
    @Resource
    private ILoginService iLoginService;
    @PostMapping(value = "login/sign")
    public ReturnEntity login(HttpServletRequest request){
        return iLoginService.whiteListLogin(request);
    }
}
