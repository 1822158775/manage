package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2024-08-02 11:00:15
 * 视频签到审核表
 */

public interface ISignInReviewReimbursementService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
