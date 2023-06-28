package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.GoOutToWorkReimbursement;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-06-26 15:54:22
 * 出差审核表
 */

public interface IGoOutToWorkReimbursementMapper extends BaseMapper<GoOutToWorkReimbursement> {
    List<GoOutToWorkReimbursement> queryAll(Map map);
    Integer queryCount(Map map);
}
