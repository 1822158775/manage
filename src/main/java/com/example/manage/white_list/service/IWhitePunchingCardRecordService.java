package com.example.manage.white_list.service;

import com.example.manage.util.entity.ReturnEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023/5/5
 */

public interface IWhitePunchingCardRecordService {
    ReturnEntity methodMasterT(HttpServletRequest request, String name);
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
