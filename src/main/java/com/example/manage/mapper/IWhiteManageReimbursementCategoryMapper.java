package com.example.manage.mapper;

import com.example.manage.entity.ManageReimbursementCategory;

import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/4/12
 */

public interface IWhiteManageReimbursementCategoryMapper {
    List<ManageReimbursementCategory> queryAll(Map map);
    Integer queryCount(Map map);
}
