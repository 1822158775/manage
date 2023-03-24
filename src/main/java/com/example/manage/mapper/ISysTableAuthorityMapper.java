package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.SysTableAuthority;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-03-24 15:26:30
 * 角色权限
 */

public interface ISysTableAuthorityMapper extends BaseMapper<SysTableAuthority> {
    List<SysTableAuthority> queryAll(Map map);
    Integer queryCount(Map map);
}
