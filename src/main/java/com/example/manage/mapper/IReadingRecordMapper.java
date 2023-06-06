package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.ReadingRecord;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-06-01 14:43:43
 * 阅读记录
 */

public interface IReadingRecordMapper extends BaseMapper<ReadingRecord> {
    List<ReadingRecord> queryAll(Map map);
    Integer queryCount(Map map);
}
