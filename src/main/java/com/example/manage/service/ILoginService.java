package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @avthor 潘小章
 * @date 2022/4/14
 */

public interface ILoginService {
    ReturnEntity login(HttpServletRequest request, HttpSession session);
}
