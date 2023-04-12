package com.example.manage.white_list.service;

import com.example.manage.util.entity.ReturnEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023/4/11
 */

public interface IWhiteManageReimbursementRecordService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
    ReturnEntity methodMasterT(HttpServletRequest request, String name);
}
