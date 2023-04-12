package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.ManageReimbursementRecord;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-04-11 15:00:11
 * 申请报销记录管理
 */

public interface IManageReimbursementRecordMapper extends BaseMapper<ManageReimbursementRecord> {
    List<ManageReimbursementRecord> queryAll(Map map);
    Integer queryCount(Map map);
}
