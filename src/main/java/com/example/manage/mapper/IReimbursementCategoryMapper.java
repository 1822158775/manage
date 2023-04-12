package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.ReimbursementCategory;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-04-11 15:26:31
 * 报销记录关联类目
 */

public interface IReimbursementCategoryMapper extends BaseMapper<ReimbursementCategory> {
    List<ReimbursementCategory> queryAll(Map map);
    Integer queryCount(Map map);
}
