package com.example.manage.mapper;

import com.example.manage.entity.PunchingCardRecord;

import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/5/6
 */

public interface IWhitePunchingCardRecordMapper {
    List<PunchingCardRecord> queryAll(Map map);
}
