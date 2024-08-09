package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.PunchingCardRecordReimbursement;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2024-08-09 15:11:58
 * 签到审核表
 */

public interface IPunchingCardRecordReimbursementMapper extends BaseMapper<PunchingCardRecordReimbursement> {
    List<PunchingCardRecordReimbursement> queryAll(Map map);
    Integer queryCount(Map map);
}
