package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.FeedbackRecordSheet;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-08-01 10:52:38
 * 反馈记录表
 */

public interface IFeedbackRecordSheetMapper extends BaseMapper<FeedbackRecordSheet> {
    List<FeedbackRecordSheet> queryAll(Map map);
    Integer queryCount(Map map);
}
