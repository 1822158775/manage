package com.example.manage.mapper;

import com.example.manage.entity.FurloughRecord;

import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2024/2/19
 */

public interface WhiteGoOutToWorkTwoMapper {
    List<FurloughRecord> queryAll(Map map);
    Integer queryCount(Map map);
}
