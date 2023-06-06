package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-06-01 14:43:43
 * 阅读记录
 */

public interface IReadingRecordService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
