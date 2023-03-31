package com.example.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.SysPersonnel;

import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/3/23
 */

public interface ISysPersonnelMapper extends BaseMapper<SysPersonnel> {
    List<SysPersonnel> queryAll(Map map);
    Integer queryCount(Map map);
    List<SysPersonnel> queryCard(Map jsonMap);
}
