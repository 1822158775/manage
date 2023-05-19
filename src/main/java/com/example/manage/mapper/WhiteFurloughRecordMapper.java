package com.example.manage.mapper;


import com.example.manage.entity.FurloughRecord;

import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/5/17
 */

public interface WhiteFurloughRecordMapper {
    List<FurloughRecord> queryAll(Map map);
    Integer queryCount(Map map);
}
