package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.CheckInTime;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-05-10 17:53:30
 * 打卡时间表
 */

public interface ICheckInTimeMapper extends BaseMapper<CheckInTime> {
    List<CheckInTime> queryAll(Map map);
    Integer queryCount(Map map);
}
