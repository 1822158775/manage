package com.example.manage.mapper;

import com.example.manage.entity.ManageReimbursementRecord;

import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/4/13
 * 申请报销记录管理
 */

public interface WhiteManageReimbursementRecordMapper {
    Integer queryCount(Map map);
    List<ManageReimbursementRecord> queryAll(Map map);
    List<ManageReimbursementRecord> queryApproverAll(Map map);
}
