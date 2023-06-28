package com.example.manage.mapper;

import com.example.manage.entity.FurloughRecord;

import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/6/26
 */

public interface WhiteGoOutToWorkMapper {
    List<FurloughRecord> queryAll(Map map);
    Integer queryCount(Map map);
}
