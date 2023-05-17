package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-05-16 09:47:03
 * 补卡
 */

public interface ICardReplacementRecordService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
