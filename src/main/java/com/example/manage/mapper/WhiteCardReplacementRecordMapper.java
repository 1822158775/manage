package com.example.manage.mapper;

import com.example.manage.entity.CardReplacementRecord;

import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/5/16
 */

public interface WhiteCardReplacementRecordMapper {
    List<CardReplacementRecord> queryAll(Map map);
    Integer queryCount(Map map);
}
