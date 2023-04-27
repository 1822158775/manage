package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.DispatchApplicationReimbursement;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-04-26 11:17:41
 * 调派关联审批人
 */

public interface IDispatchApplicationReimbursementMapper extends BaseMapper<DispatchApplicationReimbursement> {
    List<DispatchApplicationReimbursement> queryAll(Map map);
    Integer queryCount(Map map);
    Integer queryMax(Map map);
}
