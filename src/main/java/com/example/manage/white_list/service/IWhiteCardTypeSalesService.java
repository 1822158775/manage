package com.example.manage.white_list.service;

import com.example.manage.util.entity.ReturnEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023/6/19
 */

public interface IWhiteCardTypeSalesService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
