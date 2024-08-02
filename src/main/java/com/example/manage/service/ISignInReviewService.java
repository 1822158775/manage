package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2024-08-02 10:59:24
 * 视频签到申请表
 */

public interface ISignInReviewService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
