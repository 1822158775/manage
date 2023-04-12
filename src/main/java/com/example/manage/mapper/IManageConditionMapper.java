package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.ManageCondition;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-04-11 14:24:25
 * 特殊条件管理
 */

public interface IManageConditionMapper extends BaseMapper<ManageCondition> {
    List<ManageCondition> queryAll(Map map);
    Integer queryCount(Map map);
}
