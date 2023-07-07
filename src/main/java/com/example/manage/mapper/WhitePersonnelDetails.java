package com.example.manage.mapper;

import com.example.manage.entity.PunchingCardRecord;

import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/7/3
 */

public interface WhitePersonnelDetails {
    List<PunchingCardRecord> queryAll(Map map);
}
