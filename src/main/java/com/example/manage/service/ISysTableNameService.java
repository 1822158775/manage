package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023-03-24 15:05:37
 * 数据表名称
 */

public interface ISysTableNameService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
    ReturnEntity methodMasterT(HttpServletRequest request, String name);
}
