package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.FurloughRecord;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-05-17 11:15:58
 * 请假记录表
 */

public interface IFurloughRecordMapper extends BaseMapper<FurloughRecord> {
    List<FurloughRecord> queryAll(Map map);
    Integer queryCount(Map map);
}
