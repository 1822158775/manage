package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.BalanceRecordManagement;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-03-27 11:16:09
 * 余额记录管理
 */

public interface IBalanceRecordManagementMapper extends BaseMapper<BalanceRecordManagement> {
    List<BalanceRecordManagement> queryAll(Map map);
    Integer queryCount(Map map);
}