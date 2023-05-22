package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-05-17 11:45:41
 * 请假审核表
 */

public interface IFurloughReimbursementService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
