package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.DispatchApplicationManagement;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-03-29 11:28:23
 * 调派管理
 */

public interface IDispatchApplicationManagementMapper extends BaseMapper<DispatchApplicationManagement> {
    List<DispatchApplicationManagement> queryAll(Map map);
    Integer queryCount(Map map);
}
