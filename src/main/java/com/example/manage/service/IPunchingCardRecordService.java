package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023-05-05 15:41:39
 * 打卡记录表
 */

public interface IPunchingCardRecordService {
    ReturnEntity methodMaster(HttpServletRequest request, String name);
    ReturnEntity ceshi(Map map, String name);
}
