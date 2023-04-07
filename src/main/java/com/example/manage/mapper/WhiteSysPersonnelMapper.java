package com.example.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.SysPersonnel;

import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/4/6
 */

public interface WhiteSysPersonnelMapper extends BaseMapper<SysPersonnel> {
    List<SysPersonnel> queryAll(Map map);
}
