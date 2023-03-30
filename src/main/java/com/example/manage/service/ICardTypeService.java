package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-03-30 14:11:53
 * 卡种管理
 */

public interface ICardTypeService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
