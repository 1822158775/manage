package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.UnbindRecord;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-10-31 09:14:49
 * 解绑记录表
 */

public interface IUnbindRecordMapper extends BaseMapper<UnbindRecord> {
    List<UnbindRecord> queryAll(Map map);
    Integer queryCount(Map map);
}
