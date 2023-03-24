package com.example.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.SysRole;

import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/3/23
 */

public interface ISysRoleMapper extends BaseMapper<SysRole> {
    List<SysRole> queryAll(Map map);
    Integer queryCount(Map map);
    Integer updateInsertLevelAugment(Integer integer);
}
