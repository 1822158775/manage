package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-05-16 10:30:12
 * 补卡审核人
 */

public interface ICardReplacementReimbursementService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
}
