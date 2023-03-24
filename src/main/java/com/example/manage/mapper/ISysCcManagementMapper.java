package com.example.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.SysCcManagement;

import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/3/24
 */

public interface ISysCcManagementMapper extends BaseMapper<SysCcManagement> {
    List<SysCcManagement> queryAll(Map map);
    Integer queryCount(Map map);
}
