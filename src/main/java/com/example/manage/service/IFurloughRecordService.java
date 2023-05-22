package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-05-17 11:15:58
 * 请假记录表
 */

public interface IFurloughRecordService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
