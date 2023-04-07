package com.example.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.ManageDimission;

import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/4/6
 */

public interface WhiteManageDimissionMapper extends BaseMapper<ManageDimission> {
    ManageDimission queryAll(Map map);
}
