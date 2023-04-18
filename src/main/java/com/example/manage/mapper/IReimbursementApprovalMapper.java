package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.ReimbursementApproval;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-04-11 15:22:19
 * 报销记录关联审批人进行审批
 */

public interface IReimbursementApprovalMapper extends BaseMapper<ReimbursementApproval> {
    List<ReimbursementApproval> queryAll(Map map);
    Integer queryCount(Map map);
    Integer queryMax(Map map);
}
