package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.ManageReimbursementCategory;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-04-10 18:23:08
 * 类目管理
 */

public interface IManageReimbursementCategoryMapper extends BaseMapper<ManageReimbursementCategory> {
    List<ManageReimbursementCategory> queryAll(Map map);
    Integer queryCount(Map map);
}
