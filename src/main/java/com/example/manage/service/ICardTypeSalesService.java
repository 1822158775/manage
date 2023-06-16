package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-06-16 15:07:18
 * 信用卡关联权益
 */

public interface ICardTypeSalesService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
