package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.DivisionPersonnel;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-06-21 17:21:22
 * 总经理关联部门
 */

public interface IDivisionPersonnelMapper extends BaseMapper<DivisionPersonnel> {
    List<DivisionPersonnel> queryAll(Map map);
    Integer queryCount(Map map);
}
