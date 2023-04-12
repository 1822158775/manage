package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-04-11 15:34:07
 * 图片存储
 */

public interface IReimbursementImageService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
