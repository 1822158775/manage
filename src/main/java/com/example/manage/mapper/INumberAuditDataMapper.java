package com.example.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.number.AuditDataNumber;

import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/4/4
 */

public interface INumberAuditDataMapper extends BaseMapper<AuditDataNumber> {
    AuditDataNumber queryOne(Map map);
}
