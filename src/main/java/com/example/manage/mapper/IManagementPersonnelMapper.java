package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.ManagementPersonnel;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-04-06 14:05:07
 * 项目关联人员
 */

public interface IManagementPersonnelMapper extends BaseMapper<ManagementPersonnel> {
    List<ManagementPersonnel> queryAll(Map map);
    Integer queryCount(Map map);
}
