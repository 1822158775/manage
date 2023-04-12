package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-04-11 16:02:00
 * 银行账户管理
 */

public interface IManageBankAccountService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
