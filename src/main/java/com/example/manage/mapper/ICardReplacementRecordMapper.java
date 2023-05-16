package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.CardReplacementRecord;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-05-16 09:47:03
 * 补卡
 */

public interface ICardReplacementRecordMapper extends BaseMapper<CardReplacementRecord> {
    List<CardReplacementRecord> queryAll(Map map);
    Integer queryCount(Map map);
}
