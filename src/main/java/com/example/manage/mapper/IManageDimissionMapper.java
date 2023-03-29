package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.ManageDimission;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-03-29 11:16:51
 * 离职申请管理
 */

public interface IManageDimissionMapper extends BaseMapper<ManageDimission> {
    List<ManageDimission> queryAll(Map map);
    Integer queryCount(Map map);
}
