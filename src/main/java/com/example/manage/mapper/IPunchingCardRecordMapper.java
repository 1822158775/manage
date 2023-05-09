package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.PunchingCardRecord;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-05-05 15:41:39
 * 打卡记录表
 */

public interface IPunchingCardRecordMapper extends BaseMapper<PunchingCardRecord> {
    List<PunchingCardRecord> queryAll(Map map);
    Integer queryCount(Map map);
}
