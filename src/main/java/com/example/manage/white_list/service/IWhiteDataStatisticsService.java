package com.example.manage.white_list.service;

import com.example.manage.util.entity.ReturnEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2023/4/19
 * 数据统计
 */

public interface IWhiteDataStatisticsService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);

}
