package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-08-01 10:52:38
 * 反馈记录表
 */

public interface IFeedbackRecordSheetService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
