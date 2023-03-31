package com.example.manage.white_list.controller;

import com.example.manage.white_list.service.IWhiteSysPersonnelService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @avthor 潘小章
 * @date 2023/3/31
 */

@RestController
@RequestMapping(value = "/api/sys_management/")
public class WhiteSysPersonnelController {

    @Resource
    private IWhiteSysPersonnelService iWhiteSysPersonnelService;
}
