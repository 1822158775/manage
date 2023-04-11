package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.ManageRM;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-04-10 18:27:51
 * 类目关联审批人管理
 */

public interface IManageRMMapper extends BaseMapper<ManageRM> {
    List<ManageRM> queryAll(Map map);
    Integer queryCount(Map map);
}
